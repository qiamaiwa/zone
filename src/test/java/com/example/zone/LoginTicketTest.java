package com.example.zone;

import com.example.zone.dao.DiscussPostMapper;
import com.example.zone.dao.LoginTicketMapper;
import com.example.zone.dao.UserMapper;
import com.example.zone.entity.DiscussPost;
import com.example.zone.entity.LoginTicket;
import com.example.zone.utils.ZoneUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ZoneApplication.class)
public class LoginTicketTest {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired

    private DiscussPostMapper discussPostMapper;

    @Test
    public  void test(){
//        LoginTicket loginTicket=new LoginTicket();
//        loginTicket.setUserId(101);
//        loginTicket.setStatus(0);
//        loginTicket.setTicket("abc");
//        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
//
//        loginTicketMapper.insertLoginTicket(loginTicket);
////
        LoginTicket t=loginTicketMapper.selectByTicket("abc");
        System.out.println(t);
        loginTicketMapper.updateStatus("abc",0);
        LoginTicket t1=loginTicketMapper.selectByTicket("abc");
        System.out.println(t1);


    }

    @Test
    public  void testMd5(){
        String  pwd="twobno1557";
        String salt=userMapper.selectByName("liliqin").getSalt();
        System.out.println(ZoneUtil.md5(pwd + salt).equals("b298663b42fe38802f8a5d13db101049"));


    }

    @Test
    public  void testInsertPost(){
        DiscussPost post=new DiscussPost();
        post.setContent("我找到工作啦");
        post.setUserId(149);
        post.setTitle("YES");
        post.setType(0);
        post.setCreateTime(new Date());
        post.setStatus(0);

        discussPostMapper.insertDiscussPost(post);





    }

}
