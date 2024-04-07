package com.example.sender_consumer_app.sender;

import com.example.sender_consumer_app.dto.notification.NotificationReadDto;
import com.example.sender_consumer_app.util.MessageTemplateFactory;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMailSender {

    private final JavaMailSender mailSender;
    private final MessageTemplateFactory messageTemplateFactory;

    @Value("${spring.mail.username}")
    private String EMAIL_FROM;
    private static final String SUBJECT = "Notification message";

    public void sendNotification(NotificationReadDto dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String notificationMessage = messageTemplateFactory.createMessageTemplate(dto);

            helper.setFrom(EMAIL_FROM);
            helper.setTo(dto.getEmail());
            helper.setSubject(SUBJECT);
            helper.setText(notificationMessage, true);

            mailSender.send(message);
            log.info("Message successfully sent to email: " + dto.getEmail());
        } catch (Exception ignored) {
            log.error("Email didnt work");
        }
    }

}