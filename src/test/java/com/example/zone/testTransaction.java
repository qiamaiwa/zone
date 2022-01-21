package com.example.zone;

import com.example.zone.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ZoneApplication.class)
public class testTransaction {

    @Autowired
    private AlphaService alphaService;

    @Test
    public  void test(){
        alphaService.save1();
    }
}
