package com.ibtissam.ibtissambank.service;

import com.ibtissam.ibtissambank.entity.TokenBlacklist;
import com.ibtissam.ibtissambank.repository.TokenBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenBlacklistService {
    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    public void blacklistToken(String token, Date expiryDate) {

        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setExpiryDate(expiryDate);
        tokenBlacklistRepository.save(tokenBlacklist);
    }

    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token).isPresent();
    }
}
