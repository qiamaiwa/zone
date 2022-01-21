package com.example.zone.utils;
import com.jhlabs.image.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Component
public class MailClient {

    private  static  final Logger logger= (Logger) LoggerFactory.getLogger(MailClient.class);


    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.mail.username}")
    private  String sender;





    public void  sendMail(String to,String subject,String content){

        MimeMessage message=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        try {
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);

            javaMailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("邮件发送失败"+e.getMessage());
        }


    }








}
