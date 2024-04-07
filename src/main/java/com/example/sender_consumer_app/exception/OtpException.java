package com.example.sender_consumer_app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OtpException extends RuntimeException {
    private final OtpErrorType otpErrorCode;
    private HttpStatus httpStatus;

    public OtpException(OtpErrorType errorCode){
        super(errorCode.getErrorString());
        this.otpErrorCode = errorCode;
    }

    public OtpException(OtpErrorType errorCode, HttpStatus httpStatus) {
        this.otpErrorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
