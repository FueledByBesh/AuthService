package com.lostedin.ecosystem.authservice.model;

import com.lostedin.ecosystem.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@RequiredArgsConstructor
public class Helper {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3:00 ночи
    public void deleteExpiredTokens() {
        System.out.println("Deleting expired tokens...");
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }

}
