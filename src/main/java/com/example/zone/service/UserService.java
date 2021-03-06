package com.example.zone.service;

import com.example.zone.dao.DiscussPostMapper;
import com.example.zone.dao.LoginTicketMapper;
import com.example.zone.dao.UserMapper;
import com.example.zone.entity.DiscussPost;
import com.example.zone.entity.LoginTicket;
import com.example.zone.entity.User;
import com.example.zone.utils.MailClient;
import com.example.zone.utils.RedisKeyUtil;
import com.example.zone.utils.ZoneConstant;
import com.example.zone.utils.ZoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.management.ThreadInfo;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class UserService  implements ZoneConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostMapper discussPostMapper;



    @Value("${server.servlet.context-path}")
    private  String contextPath;

    @Value("${zone.path.domain}")
    private  String  domain;


    public User findUserById(int id) {
        //        return userMapper.selectById(id);


        // 尝试用redis缓存用户
        User user=getCache(id);
        if(user==null){
            user=initCache(id);
        }
        return  user;

    }
    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        if(user==null){
            throw  new IllegalArgumentException("参数值不能为空！");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("userMessage","账号不能为空");
            return  map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("passWordMessage","密码不能为空");
            return  map;
        }

        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }


        // 验证用户
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(ZoneUtil.generateUUID().substring(0, 5));
        user.setPassword(ZoneUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(ZoneUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        //在数据库汇总插入邮件
        userMapper.insertUser(user);

        // 激活邮件

        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        context.setVariable("url", url);// http://localhost:8080/community/activation/101/code
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;


    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return  ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            //用redis缓存用户信息（对用户做修改是需要删除缓存）
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAILURE;

        }
    }



    public  Map<String,Object> login(String username,String password,int expiredSeconds) {

        Map<String ,Object> map=new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("usernameMsg", "该账号未激活!");
            return map;
        }

        // 验证密码
        password = ZoneUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(ZoneUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
//        loginTicket存在mysql里
//        loginTicketMapper.insertLoginTicket(loginTicket);

        //用redis存loginTicket，会把他序列化成一个json对象
        String redisKey= RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey,loginTicket);


        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String tikcket) {

//        loginTicketMapper.updateStatus(tikcket,1);
        String redisKey=RedisKeyUtil.getTicketKey(tikcket);
        LoginTicket loginTicket=(LoginTicket) redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey,loginTicket);
    }



   public  LoginTicket findLoginTicket(String ticket){
//        return loginTicketMapper.selectByTicket(ticket);
       String redisKey=RedisKeyUtil.getTicketKey(ticket);
       return  (LoginTicket)redisTemplate.opsForValue().get(redisKey);
    }
   public  int  updataHeader(int id,String headUrl){

        int rows=userMapper.updateHeader(id,headUrl);
       //用redis缓存用户信息（对用户做修改是需要删除缓存）
       clearCache(id);
       return  rows;

   }


    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    // 重置密码
    public Map<String, Object> resetPassword(String email, String password) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(email)) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证邮箱
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            map.put("emailMsg", "该邮箱尚未注册!");
            return map;
        }

        // 重置密码
        password = ZoneUtil.md5(password + user.getSalt());
        userMapper.updatePassword(user.getId(), password);

        map.put("user", user);
        return map;
    }
    //1.优先从缓存里取值
    public User getCache(int userId){
        String redisKey=RedisKeyUtil.getUserKey(userId);
        return  (User) redisTemplate.opsForValue().get(redisKey);
    }
    //2.取不到时初始化缓存数据
    public User initCache(int userId){
        String redisKey=RedisKeyUtil.getUserKey(userId);
        User user=userMapper.selectById(userId);
        redisTemplate.opsForValue().set(redisKey,user, 60*60,TimeUnit.SECONDS);
        return  user;
    }

    //3.数据变更时清除缓存数据
    public void clearCache(int userId){
        String redisKey=RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);

    }

}
