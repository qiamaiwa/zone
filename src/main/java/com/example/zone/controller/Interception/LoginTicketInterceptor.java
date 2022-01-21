package com.example.zone.controller.Interception;

import com.example.zone.entity.LoginTicket;
import com.example.zone.entity.User;
import com.example.zone.service.UserService;
import com.example.zone.utils.CookieUtil;
import com.example.zone.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
public class LoginTicketInterceptor  implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

//        从请求中获取cookie
        String ticket= CookieUtil.getValue(request,"ticket");

        if(ticket!=null){
            // 从数据库中查找凭证
            LoginTicket loginTicket=userService.findLoginTicket(ticket);
            if(loginTicket!=null && loginTicket.getStatus()==0 && loginTicket.getExpired().after(new Date())){
                // 根据凭证查找用户
                User user=userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }
        return true;
    }
    //在模板引擎之前就要用这个用户
    @Override
    public  void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    @Nullable ModelAndView modelAndView) throws Exception {

        User user= hostHolder.getUser();
        if(user!=null && modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }




    }











}
