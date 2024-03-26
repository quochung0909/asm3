package com.nqh.asm3.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendHtmlMessage(String to, String subject, String html);

    void sendAttachmentMessage(String to, String subject, String text, MultipartFile pathToAttachment);

}
