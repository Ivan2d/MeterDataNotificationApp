package com.example.sender_consumer_app.sender;

import com.example.sender_consumer_app.dto.otp.OtpDataSendDto;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpMailSender {

    private final JavaMailSender mailSender;

    public void sendOtp(OtpDataSendDto dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(dto.getEmailFrom());
            helper.setTo(dto.getDestination());
            helper.setSubject(dto.getSubject());
            helper.setText(dto.getText(), true);

            mailSender.send(message);
        } catch (Exception ignored) {
            log.error("Email didnt work");
        }
    }
}
