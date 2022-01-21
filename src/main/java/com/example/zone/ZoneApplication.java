package com.example.zone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

//@SpringBootApplication

//配置文件 规定了扫描的包，默认是启动类的包以及子包下的所有类。
@SpringBootApplication
public class ZoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZoneApplication.class, args);
    }

}
