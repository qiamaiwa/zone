package com.example.zone.utils;


import com.example.zone.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

//持有用户信息 用于代替session对象
@Component
public class HostHolder {

    private ThreadLocal<User> users=new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }



}
