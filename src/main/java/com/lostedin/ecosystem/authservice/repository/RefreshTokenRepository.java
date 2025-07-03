package com.lostedin.ecosystem.authservice.repository;

import com.lostedin.ecosystem.authservice.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findById(UUID id);
    void deleteByToken(String token);
    void deleteById(UUID tokenId);

    @Modifying
    @Query("DELETE FROM RefreshTokenEntity t WHERE t.expiresAt < :now")
    void deleteAllExpiredTokens(@Param("now") Instant now);
}
