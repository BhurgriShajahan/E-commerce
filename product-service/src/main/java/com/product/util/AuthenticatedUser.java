package com.product.util;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUser {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    public Long getAuthUserId() {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.extractAllClaims(token);

            // Assuming you store "userId" in JWT claims
            Object userIdClaim = claims.get("userId");

            if (userIdClaim instanceof String) {
                return Long.parseLong((String) userIdClaim);
            } else if (userIdClaim instanceof Integer) {
                return ((Integer) userIdClaim).longValue();
            } else if (userIdClaim instanceof Long) {
                return (Long) userIdClaim;
            } else {
                throw new IllegalStateException("Invalid userId type in JWT claims: " + userIdClaim);
            }
        }

        throw new IllegalStateException("Authorization header is missing or invalid.");
    }
}
