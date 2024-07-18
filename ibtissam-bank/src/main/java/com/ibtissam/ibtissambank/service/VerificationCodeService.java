package com.ibtissam.ibtissambank.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerificationCodeService {

    private final int VERIFICATION_CODE_LENGTH = 6;

    // Generate a random verification code
    public String generateVerificationCode() {
        StringBuilder verificationCode = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            verificationCode.append(random.nextInt(10)); // Append a random digit (0-9)
        }
        return verificationCode.toString();
    }
}
