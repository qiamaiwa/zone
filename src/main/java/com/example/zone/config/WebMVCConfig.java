package com.example.zone.config;

import com.example.zone.annotation.LoginRequired;
import com.example.zone.controller.Interception.LoginRequiredInterceptor;
import com.example.zone.controller.Interception.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;


    @Override
    public  void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginTicketInterceptor).excludePathPatterns("/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor).excludePathPatterns("/**/*.css","/**/*.png","/**/*.jpg","/**/*.jpeg");

    }

}
