package com.example.usermanagementservice.services.mail;


import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.services.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService{
    final JavaMailSender mailSender;

    final UserService userService;

    @Value( "${spring.mail.username}" )
    String userName;

    @Value("${spring.mail.password}")
    String password;

    @Value("${spring.mail.host")
    String host;

    String response;

    @Override
    public void sendMail(String username) {
        MimeMessage message = mailSender.createMimeMessage();
        User user=userService.getUser(username);
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);



                helper.setTo(user.getEmail());
                helper.setFrom(userName);
                helper.setSubject("Username or Password is wrong");
                helper.setText("Username or password is incorrect, please check your password");

                mailSender.send(message);
                response = "Email has been send to :"+user.getEmail();



        } catch (MessagingException e) {
            response = "Email has been send to :"+user.getEmail();
        }

    }
}
