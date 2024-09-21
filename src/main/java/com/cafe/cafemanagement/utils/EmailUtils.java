package com.cafe.cafemanagement.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailUtils {
    @Autowired
    private JavaMailSender eMailSender;

    public void sendSimpleMessage(String toWhom, String subject, String text, List<String> list) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("kageyama69183@gmail.com");
        mailMessage.setTo(toWhom);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        if(list!=null && list.size()>0)
            mailMessage.setCc(getCcArray(list));

        eMailSender.send(mailMessage);
    }

    private String[] getCcArray(List<String> list) {
        String[] ccArray = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            ccArray[i] = list.get(i);
        }
        return ccArray;
    }
}
