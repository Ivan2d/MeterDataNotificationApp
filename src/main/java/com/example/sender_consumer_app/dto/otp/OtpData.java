package com.example.sender_consumer_app.dto.otp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpData {
    private String uuid;
    private String userUid;
    private String destination;
    private String otpCode;
    private Integer length;
    private Integer attempts;
    private Long attemptsUsed;
    private Integer ttl;
    private Integer delay;
    private Boolean force;
    private Long currentDateInSeconds;
}