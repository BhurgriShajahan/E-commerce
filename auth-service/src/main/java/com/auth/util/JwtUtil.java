package com.auth.util;

import com.auth.enums.TokenType;
import com.auth.exception.CustomJwtException;
import com.auth.model.entities.TokenExpiration;
import com.auth.repository.TokenExpirationRepository;
import com.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;
//    @Autowired
//    BlackListTokenService tokenService;
    @Autowired
    private final TokenExpirationRepository tokenExpirationRepository;

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public String generateToken(String username, Set<GrantedAuthority> roles, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put("userId", userId);

        Optional<TokenExpiration> tokenExpiration = tokenExpirationRepository.findByTokenType(TokenType.BEARER_TOKEN);
        if(tokenExpiration.isPresent()){

            TokenExpiration tokenExpirationEntity = tokenExpiration.get();
            boolean hasAdminRole = roles.stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            if(!hasAdminRole){
                return Jwts.builder()
                        .setClaims(claims)
                        .setSubject(username)
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * tokenExpirationEntity.getExpirationTimeInMins()))
                        //.signWith(SECRET_KEY)
                        .signWith(getSignKey())
                        .compact();
            }
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))
                //.signWith(SECRET_KEY)
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = getClaims(token);
        return (List<String>) claims.get("roles");
    }

//    private boolean findBySessionToken(String sessionToken) {
//        boolean tokenBlacklisted = tokenService.isTokenBlacklisted(sessionToken);
//
//        if(tokenBlacklisted){
//            return false;
//        }
//
//        return userRepository.isValidToken(sessionToken,System.currentTimeMillis());
//    }
//    public boolean validateToken(String token, String username) {
//        final String extractedUsername = extractUsername(token);
//        if (findBySessionToken(token)) {
//            return (extractedUsername.equals(username) && !isTokenExpired(token));
//        }
//        return false;
//    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
    public Claims getClaims(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(getSignKey())
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException exception){
        //throw new RuntimeException(exception);
        throw new CustomJwtException("JWT token has expired. Please login again.", exception);
        }catch (SignatureException e) {
        // Handle invalid JWT signature (forged or tampered token)
        throw new CustomJwtException("Invalid JWT signature.", e);
        } catch (MalformedJwtException e) {
        // Handle malformed tokens (incorrect format)
        throw new CustomJwtException("Malformed JWT token.", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
         //   throw new CustomJwtException("Invalid JWT token", e);
        }
    }
    public Date getTokenExpireTime(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

