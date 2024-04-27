package com.video.app.dto.mail;

public record MailSendMessageDto(
        String to,
        String subject,
        String message
) {
}
