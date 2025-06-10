package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.entity.RefreshTokenEntity;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void createRefreshToken(UUID userId, String token, long expirationMillis) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserId(userId);
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(expirationMillis, ChronoUnit.MILLIS));
        try{
            refreshTokenRepository.save(refreshTokenEntity);
        }catch (Exception e){
            throw new ServiceException(500, "Error saving refresh token");
        }
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshTokenEntity> findById(UUID id) {
        return refreshTokenRepository.findById(id);
    }

    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteRefreshToken(UUID tokenId) {
        refreshTokenRepository.deleteById(tokenId);
    }

    public void deleteRefreshTokenByUserId(UUID userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    public boolean isTokenExpired(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokenEntity.getExpiresAt().isBefore(Instant.now());
    }

    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }

}
