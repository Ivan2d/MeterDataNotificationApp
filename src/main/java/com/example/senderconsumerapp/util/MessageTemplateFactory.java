package com.example.senderconsumerapp.util;

import com.example.senderconsumerapp.dto.NotificationReadDto;
import org.springframework.stereotype.Component;

@Component
public class MessageTemplateFactory {

    public String createMessageTemplate(NotificationReadDto notificationDto) {
        String message = """
                <h1>Notification message</h1>
                <p>Name: %s</p>
                <p>Description: %s</p>
                <p>Date: %s</p>
                <p>Status: %s</p>
                """;
        return String.format(message,
                notificationDto.getName(), notificationDto.getDescription(),
                notificationDto.getCreatedAt(), notificationDto.getNotificationType());
    }
}