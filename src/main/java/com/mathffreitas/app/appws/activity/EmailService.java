package com.mathffreitas.app.appws.activity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendVerification(String to, String email) {
            try{
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(email, true);
                helper.setTo(to);
                helper.setSubject("Confirm your email");
                helper.setFrom("zvetfuze@gmail.com");
                mailSender.send(mimeMessage);
            }catch(MessagingException e) {
                LOGGER.error("Failed to send", e);
                throw new IllegalStateException("Failed to send email");
            }
    }

    @Override
    @Async
    public boolean sendPasswordReset(String to, String email) {
        boolean returnValue = false;
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Password Reset");
            helper.setFrom("zvetfuze@gmail.com");
            mailSender.send(mimeMessage);
            returnValue = true;
        }catch(MessagingException e) {
            LOGGER.error("Failed to send", e);
            throw new IllegalStateException("Failed to send email");
        }
        return returnValue;
    }
}