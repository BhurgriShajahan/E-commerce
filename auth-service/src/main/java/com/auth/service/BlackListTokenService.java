package com.auth.service;

public interface BlackListTokenService {

    boolean isTokenBlacklisted(String token);

    public void cleanupExpiredTokens();
}
