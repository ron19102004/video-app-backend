package com.video.app.mail;

import com.video.app.dto.mail.MailSendMessageDto;
import com.video.app.entities.User;
import com.video.app.entities.VIP;
import com.video.app.services.OTPService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;


@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String USERNAME;
    @Autowired
    private OTPService otpService;
    @Autowired
    private TemplateEngine templateEngine;
    private static final String ENCODING = "UTF-8";

    private static final String THANK_FOR_REGISTERING_TEMPLATE_PATH = "mail/thank-for-register";
    private static final String THANK_FOR_REPORT_ISSUE_TEMPLATE_PATH = "mail/thank-for-report";
    private static final String OTP_TEMPLATE_PATH = "mail/otp";
    private static final String THANK_FOR_REGISTER_VIP_PATH = "mail/thank-for-register-vip";


    @Override
    public void create(MailSendMessageDto mailSendMessageDto) throws MessagingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(USERNAME);
        simpleMailMessage.setTo(mailSendMessageDto.to());
        simpleMailMessage.setSubject(mailSendMessageDto.subject());
        simpleMailMessage.setText(mailSendMessageDto.message());
        this.javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendMailThankForRegister(User user) throws MessagingException {
        String subject = "Thank " + user.getFullName() + " for Registering!";
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, ENCODING);
        mimeMessageHelper.setFrom(USERNAME);
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setSubject(subject);

        Context context = new Context();
        context.setVariable("fullName", user.getFullName());
        context.setVariable("admin", "Ron");
        String content = this.templateEngine.process(THANK_FOR_REGISTERING_TEMPLATE_PATH, context);

        mimeMessageHelper.setText(content, true);
        this.javaMailSender.send(message);
    }

    @Override
    public void sendMailForReport(String email) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, ENCODING);
        mimeMessageHelper.setFrom(USERNAME);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Thank for Report Issue");

        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("organization", "Video");
        context.setVariable("admin", "Ron");

        String content = this.templateEngine.process(THANK_FOR_REPORT_ISSUE_TEMPLATE_PATH, context);
        mimeMessageHelper.setText(content, true);
        this.javaMailSender.send(message);
    }

    @Override
    public void sendMailOTP(User user) throws MessagingException {
        String otp = this.otpService.generate(user);
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, ENCODING);
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setFrom(USERNAME);
        mimeMessageHelper.setSubject("Your OTP code");

        Context context = new Context();
        context.setVariable("otp", otp);
        context.setVariable("fullName", user.getFullName());
        context.setVariable("time_sent", new Date());
        String content = this.templateEngine.process(OTP_TEMPLATE_PATH, context);
        mimeMessageHelper.setText(content, true);
        this.javaMailSender.send(message);
    }

    @Override
    public void sendMailRegisterVIP(User user, VIP vip) throws MessagingException {
        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
        mimeMessageHelper.setSubject("Thank You for Subscribing to Our VIP Service");
        mimeMessageHelper.setFrom(USERNAME);
        mimeMessageHelper.setTo(user.getEmail());

        String issued = vip.getIssuedAt().toString();
        String expired = vip.getExpiredAt().toString();
        String benefits = "use VIP app from " + issued + " to " + expired;
        Context context = new Context();
        context.setVariable("customerName", user.getFullName());
        context.setVariable("admin", "Ron");
        context.setVariable("emailOrPhone", "ron19102004@gmail.com");
        context.setVariable("appName", "Video");
        context.setVariable("benefits", benefits);
        String content = this.templateEngine.process(THANK_FOR_REGISTER_VIP_PATH, context);
        mimeMessageHelper.setText(content,true);
        this.javaMailSender.send(message);
    }
}
