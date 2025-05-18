package com.auth.service.impl;

import com.auth.model.entities.BlacklistedToken;
import com.auth.repository.BlackListTokenRepository;
import com.auth.service.BlackListTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BlackListTokenServiceImpl implements BlackListTokenService {

    @Autowired
    private BlackListTokenRepository blackListTokenRepository;

    @Override
    public boolean isTokenBlacklisted(String token) {
        Optional<BlacklistedToken> blackListedToken = blackListTokenRepository.findByToken(token);
        return blackListedToken.isPresent() && blackListedToken.get().getExpirationTime().isAfter(LocalDateTime.now());
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = blackListTokenRepository.deleteByExpirationTimeBefore(now);
        System.out.println(deletedCount + " expired tokens deleted from the blacklist.");
    }

}
