package com.example.sender_consumer_app.controller;

import com.example.sender_consumer_app.dto.otp.OtpCheckRequestDto;
import com.example.sender_consumer_app.dto.otp.OtpCheckResponseDto;
import com.example.sender_consumer_app.dto.otp.OtpSendRequestDto;
import com.example.sender_consumer_app.dto.otp.OtpSendResponseDto;
import com.example.sender_consumer_app.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @Operation(summary = "Sent otp to user", description = "Returns data about the sent dto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect data was entered")
    })
    @PostMapping("/send")
    public OtpSendResponseDto sendOtpMessage(@RequestBody OtpSendRequestDto dto) {
        log.info("Otp controller send otp");
        return otpService.sendOtp(dto);
    }


    @Operation(summary = "Verify otp code", description = "Returns data about checking the code for validness")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The code is valid"),
            @ApiResponse(responseCode = "400", description = "Incorrect data was entered or an incorrect code was entered")
    })
    @PostMapping("/check")
    public OtpCheckResponseDto checkOtpMessage(@RequestBody OtpCheckRequestDto dto) {
        log.info("Otp controller check otp");
        return otpService.verifyOtp(dto);
    }
}
