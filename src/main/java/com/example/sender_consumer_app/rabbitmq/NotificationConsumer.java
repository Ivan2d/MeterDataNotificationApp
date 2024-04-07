package com.example.sender_consumer_app.rabbitmq;

import com.example.sender_consumer_app.dto.notification.NotificationReadDto;
import com.example.sender_consumer_app.sender.NotificationMailSender;
import com.example.sender_consumer_app.util.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationMailSender mailSender;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consumeNotification(String message) {
        try {
            NotificationReadDto dto = GsonFactory.generateGson()
                    .fromJson(message, NotificationReadDto.class);
            log.info(String.format("A message with the name and status has been received -> %s, %s",
                    dto.getName(), dto.getNotificationType()));
            mailSender.sendNotification(dto);
        } catch (Exception ex) {
            log.error("Error while consuming the message " + ex.getMessage());
        }
    }
}