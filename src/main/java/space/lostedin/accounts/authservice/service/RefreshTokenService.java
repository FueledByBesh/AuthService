package space.lostedin.accounts.authservice.service;

import space.lostedin.accounts.authservice.entity.RefreshToken;
import space.lostedin.accounts.authservice.exception.ServiceException;
import space.lostedin.accounts.authservice.repository.RefreshTokenRepository;
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
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(Instant.now().plus(expirationMillis, ChronoUnit.MILLIS));
        try{
            refreshTokenRepository.save(refreshToken);
        }catch (Exception e){
            throw new ServiceException(500, "Error saving refresh token");
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> findById(UUID id) {
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

    public boolean isTokenExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiresAt().isBefore(Instant.now());
    }

    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }

}
