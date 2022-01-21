package com.example.zone.controller.Interception;

import javafx.application.Application;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AlphaInterception  implements HandlerInterceptor {
    @Override
    public  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        return true;

    }
}
