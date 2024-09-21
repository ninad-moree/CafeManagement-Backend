package com.cafe.cafemanagement.utils;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void forgotPasswordEmail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = eMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("kageyama69183@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        
        String htmlMsg = "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to + " <br><b>Password: </b> " + password + "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg, "text/html");

        eMailSender.send(message);
    }
}
