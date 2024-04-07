package com.example.sender_consumer_app.util;

import java.util.Random;

public class DefaultOtpGenerator {
    private static final char[] ALLOWED_SYMBOLS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public String newCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random r = new Random();
        for (int i = 0; i < length; i++)
            sb.append(ALLOWED_SYMBOLS[r.nextInt(ALLOWED_SYMBOLS.length)]);
        return sb.toString();
    }
}
