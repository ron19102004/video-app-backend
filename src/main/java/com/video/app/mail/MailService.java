package com.video.app.mail;

import com.video.app.dto.mail.MailSendMessageDto;
import com.video.app.entities.User;
import jakarta.mail.MessagingException;

public interface MailService {
    void create(MailSendMessageDto mailSendMessageDto) throws MessagingException;
    void sendMailThankForRegister(User user) throws MessagingException;
    void sendMailForReport(String email) throws MessagingException;
    void sendMailOTP(User user) throws MessagingException;
}
