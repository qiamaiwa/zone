package com.example.zone.controller.Interception;

import com.example.zone.annotation.LoginRequired;
import com.example.zone.entity.User;
import com.example.zone.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Handler;

@Component
public class LoginRequiredInterceptor  implements HandlerInterceptor {


    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            Method method=handlerMethod.getMethod();
            LoginRequired loginRequired=method.getAnnotation(LoginRequired.class);
            if(loginRequired!=null && hostHolder.getUser()==null){
                response.sendRedirect(request.getContextPath());
                return false;
            }

        }

        User user=hostHolder.getUser();
        if(user!=null){

        }

        return true;
    }




}
