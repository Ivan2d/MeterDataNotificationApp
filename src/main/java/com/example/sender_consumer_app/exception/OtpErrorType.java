package com.example.sender_consumer_app.exception;

import lombok.Getter;

@Getter
public enum OtpErrorType {

    INCORRECT_OTP_UUID("Incorrect otp uuid or expiration date has expired"),
    LIMIT_OF_ATTEMPTS_EXCEEDED("Limit of attempts exceeded"),
    WRONG_NUMBER_OF_CHARACTERS("Wrong number of characters"),
    NOW_ALLOWED_TO_RESUBMIT_CODE("Not allowed to resubmit code"),
    WAITING_TIME_HAS_NOT_EXPIRED("The waiting time has not expired"),
    DIFFERENT_OTP_TYPES("Different otp types"),
    INCORRECT_OTP_DATA("Incorrect otp data"),
    INCORRECT_OTP_TYPE("Incorrect otp type");

    private final String errorString;

    OtpErrorType(String errorString) {
        this.errorString = errorString;
    }
}
