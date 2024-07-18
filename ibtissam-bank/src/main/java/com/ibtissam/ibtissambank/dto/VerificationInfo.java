package com.ibtissam.ibtissambank.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationInfo {
    private String VerificationCode;
    private long creationTime;
    private long expirationTime;

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }
}