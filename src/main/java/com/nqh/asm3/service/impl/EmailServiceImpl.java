package com.nqh.asm3.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nqh.asm3.service.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hungnqfx19911@funix.edu.vn");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
        System.out.println("Send email success");
    }

    @Override
    public void sendHtmlMessage(String to, String subject, String html) {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("hungnqfx19911@funix.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        javaMailSender.send(message);
    }

    @Override
    public void sendAttachmentMessage(String to, String subject, String text, MultipartFile pathToAttachment) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom("hungnqfx19911@funix.edu.vn");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.addAttachment("attachment", pathToAttachment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }

}
