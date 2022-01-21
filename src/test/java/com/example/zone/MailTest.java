package com.example.zone;

import com.example.zone.utils.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ZoneApplication.class)
public class MailTest {

        @Autowired
        private  MailClient mailClient;

        @Test
        public void testTextMail(){
            mailClient.sendMail("liliqin@bupt.edu.cn","TEST","welcome");
        }

        @Autowired
        private TemplateEngine templateEngine;


        @Test
        public void testHtmlMail(){

            Context context=new Context();
            context.setVariable("username","sudnay");

            //生成一个网页
            String content=templateEngine.process("/mail/demo",context);
            System.out.println(content);

            mailClient.sendMail("liliqin@bupt.edu.cn","HTML",content);


        }

}
