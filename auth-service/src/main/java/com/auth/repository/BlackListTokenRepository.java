package com.auth.repository;

import com.auth.model.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    Optional<BlacklistedToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM BlacklistedToken b WHERE b.expirationTime < :now")
    int deleteByExpirationTimeBefore(LocalDateTime now);
}

