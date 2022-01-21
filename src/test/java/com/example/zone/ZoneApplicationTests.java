package com.example.zone;

import com.example.zone.dao.AlphaDao;
import com.example.zone.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes =ZoneApplication.class)
class ZoneApplicationTests implements ApplicationContextAware {

    private  ApplicationContext applicationContext;
    @Autowired
    private AlphaDao alphaDao;
    @Autowired
    @Qualifier("Hibernate")
    private AlphaDao alphaDao1;
    @Autowired
    private AlphaService alphaService;
    @Autowired
    private SimpleDateFormat simpleDateFormat;




    @Test
    public  void test(){
//        AlphaDao  alphaDao=applicationContext.getBean(AlphaDao.class);
        System.out.println(alphaDao.select());
//
//        AlphaDao  alphaDao1=applicationContext.getBean("Hibernate",AlphaDao.class);
        System.out.println(alphaDao1.select());
//
//        AlphaService alphaService=applicationContext.getBean(AlphaService.class);
        System.out.println(alphaService);
//        AlphaService alphaService1=applicationContext.getBean(AlphaService.class);//
//        System.out.println(alphaService1);

        System.out.println(simpleDateFormat.format(new Date()));

    }





    @Test
    void contextLoads() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }
}
