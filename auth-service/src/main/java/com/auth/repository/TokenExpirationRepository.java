package com.auth.repository;

import com.auth.enums.TokenType;
import com.auth.model.entities.TokenExpiration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenExpirationRepository extends JpaRepository<TokenExpiration, Long> {
    Optional<TokenExpiration> findByTokenType(TokenType tokenType);
}
