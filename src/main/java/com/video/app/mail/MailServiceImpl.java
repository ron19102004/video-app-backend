package com.video.app.mail;

import com.video.app.dto.mail.MailSendMessageDto;
import com.video.app.entities.User;
import com.video.app.services.OTPService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;
    @Autowired
    private OTPService otpService;


    @Override
    public void create(MailSendMessageDto mailSendMessageDto) throws MessagingException {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(mailSendMessageDto.to());
        simpleMailMessage.setSubject(mailSendMessageDto.subject());
        simpleMailMessage.setText(mailSendMessageDto.message());
        this.javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendMailThankForRegister(User user) throws MessagingException {
        String subject = "Thank ${fullName} for Registering!";
        String template = "Dear ${fullName},\n" +
                "\n" +
                "Thank you for registering an account with us! We're thrilled to have you join our community.\n" +
                "Your registration is an important step towards unlocking a world of possibilities. Whether you're here for the latest updates, exclusive offers, or exciting opportunities, we're committed to providing you with a seamless experience.\n" +
                "If you have any questions or need assistance, please don't hesitate to reach out to our support team. We're here to help!\n" +
                "Once again, thank you for choosing us. We look forward to serving you and making your experience with us truly exceptional.\n" +
                "\n" +
                "Best regards,\n" +
                "${admin}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("fullName", user.getFullName());
        variables.put("admin", "Ron");

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue().toString());
            subject = subject.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(template);

        this.javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendMailForReport(String email) throws MessagingException {
        String subject = "Thank You for Reporting the Issue";
        String template = "Dear ${email},\n" +
                "\n" +
                "I hope this email finds you well.\n" +
                "I wanted to take a moment to express my sincere gratitude for bringing the issue to our attention. Your proactive approach in reporting the matter to ${name_app} is highly appreciated.\n" +
                "We understand the importance of identifying and addressing issues promptly to ensure the smooth operation of our company. Your diligence in reporting this matter reflects your commitment to our shared goals of excellence and continuous improvement.\n" +
                "Rest assured that your report is being carefully reviewed by our team, and we are taking the necessary steps to resolve the issue efficiently. If further information or clarification is needed from your end, please don't hesitate to reach out to us.\n" +
                "Once again, thank you for your attention to detail and your contribution to maintaining the quality of our operations. Your effort is invaluable to us.\n" +
                "Best regards,\n" +
                "\n" +
                "From ${admin}\n";
        Map<String, Object> variables = new HashMap<>();
        variables.put("email", email);
        variables.put("admin", "Ron");
        variables.put("name_app", "Video");

        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            template = template.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(template);

        this.javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendMailOTP(User user) throws MessagingException {
        String otp = this.otpService.generate(user);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Your OTP Code");
        simpleMailMessage.setText("Hello,\n\nHere is your OTP code: " + otp + "\n\nBest regards.");
        this.javaMailSender.send(simpleMailMessage);
    }
}
