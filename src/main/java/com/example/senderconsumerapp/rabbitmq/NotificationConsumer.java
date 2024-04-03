package com.example.senderconsumerapp.rabbitmq;

import com.example.senderconsumerapp.dto.NotificationReadDto;
import com.example.senderconsumerapp.sender.NotificationMailSender;
import com.example.senderconsumerapp.util.GsonFactory;
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