package com.example.sender_consumer_app.service;

import com.example.sender_consumer_app.dto.otp.*;
import com.example.sender_consumer_app.exception.OtpErrorType;
import com.example.sender_consumer_app.exception.OtpException;
import com.example.sender_consumer_app.sender.OtpMailSender;
import com.example.sender_consumer_app.util.DefaultOtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    @Value("${spring.mail.username}")
    private String emailFrom;
    private final OtpMailSender emailOtpSender;
    private final RedisOperations<String, OtpData> redisTemplate;
    private final DefaultOtpGenerator defaultOTPGenerator = new DefaultOtpGenerator();
    private static final Long ZERO_ATTEMPTS = 0L;

    public OtpSendResponseDto sendOtp(OtpSendRequestDto otpSendRequestDto) throws OtpException {
        try {
            validateOtpRequest(otpSendRequestDto);
            String code = defaultOTPGenerator.newCode(otpSendRequestDto.getOtpDetails().getLength());
            String messageTemplate = String.format(otpSendRequestDto.getMessageTemplate(), code);

            OtpData otpData = new OtpData(
                    otpSendRequestDto.getUuid(),
                    otpSendRequestDto.getUserUid(),
                    otpSendRequestDto.getDestination(),
                    code,
                    otpSendRequestDto.getOtpDetails().getLength(),
                    otpSendRequestDto.getOtpDetails().getAttempts(),
                    ZERO_ATTEMPTS,
                    otpSendRequestDto.getOtpDetails().getTtl(),
                    otpSendRequestDto.getOtpDetails().getDelay(),
                    otpSendRequestDto.getOtpDetails().getForce(),
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
            );

            checkDelayTime(otpSendRequestDto.getUuid());
            redisTemplate.opsForValue().set(otpSendRequestDto.getUuid(), otpData,
                    Duration.ofSeconds(otpSendRequestDto.getOtpDetails().getTtl()));
            String emailFromSend;
            if (otpSendRequestDto.getEmail() == null || otpSendRequestDto.getEmail().equals(" ")) {
                emailFromSend = emailFrom;
            } else {
                emailFromSend = otpSendRequestDto.getEmail();
            }
            OtpDataSendDto otpDataToSendDto = new OtpDataSendDto(
                    emailFromSend,
                    otpSendRequestDto.getDestination(),
                    otpSendRequestDto.getSubject(),
                    messageTemplate
            );
            emailOtpSender.sendOtp(otpDataToSendDto);
            return new OtpSendResponseDto(
                    otpSendRequestDto.getEmail(),
                    otpSendRequestDto.getUuid(),
                    code,
                    otpSendRequestDto.getOtpDetails(),
                    otpSendRequestDto.getDestination()
            );
        } catch (OtpException ex) {
            log.error("Incorrect otp send request dto");
            throw ex;
        }
    }

    public OtpCheckResponseDto verifyOtp(OtpCheckRequestDto dto) {
        checkOtpExists(dto.getOtpUid());
        var otpData = redisTemplate.opsForValue().get(dto.getOtpUid());
        try {
            validateOtpCheck(dto, otpData);
            long countUsedAttempts = otpData.getAttemptsUsed();

            if (!otpData.getOtpCode().equals(dto.getOtpCode())) {
                log.info("Checking count of attempts: Used attempts - " + countUsedAttempts + " | Total attempts - " + otpData.getAttempts());
                if (countUsedAttempts == otpData.getAttempts()) {
                    log.error("User has exceeded the maximum number of attempts");
                    throw new OtpException(OtpErrorType.LIMIT_OF_ATTEMPTS_EXCEEDED, HttpStatus.BAD_REQUEST);
                }
                countUsedAttempts += 1;
                otpData.setAttemptsUsed(countUsedAttempts);
                log.info("Incorrect otp code - attempt number :" + countUsedAttempts);

                redisTemplate.opsForValue().set(otpData.getUuid(), otpData, Duration.ofSeconds(otpData.getTtl()));
                return new OtpCheckResponseDto(countUsedAttempts, false);
            } else {
                log.info("Correct otp code - attempt number :" + countUsedAttempts);
                if (Boolean.TRUE.equals(redisTemplate.delete(otpData.getUuid()))) {
                    return new OtpCheckResponseDto(countUsedAttempts, true);
                }
            }
        } catch (OtpException e) {
            log.error("Validate otpCheck exception " + e);
            if (e.getOtpErrorCode() == OtpErrorType.INCORRECT_OTP_UUID) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getOtpErrorCode().getErrorString());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getOtpErrorCode().getErrorString());
            }
        }
        return null;
    }

    private void validateOtpCheck(OtpCheckRequestDto dto, OtpData otpData) throws OtpException {
        if (otpData == null) {
            throw new OtpException(OtpErrorType.INCORRECT_OTP_UUID);
        }
        if (!otpData.getForce() && otpData.getAttemptsUsed() > 0) {
            throw new OtpException(OtpErrorType.NOW_ALLOWED_TO_RESUBMIT_CODE);
        }
        if (dto.getOtpCode().length() != otpData.getLength()) {
            throw new OtpException(OtpErrorType.WRONG_NUMBER_OF_CHARACTERS);
        }
    }

    private void checkOtpExists(String otpUid) {
        Optional.ofNullable(redisTemplate.hasKey(otpUid))
                .flatMap(exists -> {
                    if (!exists) {
                        log.error("OTP data not found in Redis");
                        try {
                            throw new OtpException(OtpErrorType.INCORRECT_OTP_UUID, HttpStatus.BAD_REQUEST);
                        } catch (OtpException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return Optional.empty();
                });
    }

    private void checkDelayTime(String otpUid) throws OtpException {
        var otpData = redisTemplate.opsForValue().get(otpUid);
        if (otpData == null) {
            return;
        }
        log.info("Checking the timeout for a new message");
        long currentTimeSeconds = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        if ((otpData.getCurrentDateInSeconds() + otpData.getDelay()) > currentTimeSeconds) {
            log.error("The timeout for sending a new message has expired with userUid: " + otpData.getUserUid());
            throw new OtpException(OtpErrorType.WAITING_TIME_HAS_NOT_EXPIRED, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateOtpRequest(OtpSendRequestDto dto) throws OtpException {
        if (dto.getOtpDetails() == null || dto.getDestination() == null
                || dto.getOtpDetails().getLength() == null || dto.getOtpDetails().getLength() < 1
                || dto.getOtpDetails().getAttempts() == null || dto.getOtpDetails().getAttempts() < 1
                || dto.getOtpDetails().getDelay() == null || dto.getOtpDetails().getDelay() < 0
                || dto.getOtpDetails().getTtl() == null || dto.getOtpDetails().getTtl() < 0 ||
                dto.getDestination().equals(" ")) {
            throw new OtpException(OtpErrorType.INCORRECT_OTP_DATA, HttpStatus.BAD_REQUEST);
        }
    }

}
