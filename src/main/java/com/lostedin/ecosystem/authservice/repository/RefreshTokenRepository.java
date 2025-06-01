package com.lostedin.ecosystem.authservice.repository;

import com.lostedin.ecosystem.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findById(UUID id);
    List<RefreshToken> findByUserId(UUID userId);
    void deleteByToken(String token);
    void deleteById(UUID tokenId);
    void deleteByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now")
    void deleteAllExpiredTokens(@Param("now") Instant now);
}
