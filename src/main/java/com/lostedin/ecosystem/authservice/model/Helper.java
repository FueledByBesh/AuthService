package com.lostedin.ecosystem.authservice.model;

import com.lostedin.ecosystem.authservice.enums.OAuthClientAccessType;
import com.lostedin.ecosystem.authservice.enums.OAuthResponseType;
import com.lostedin.ecosystem.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class Helper {


    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Каждый день в 3:00 ночи
    public void deleteExpiredTokens() {
        System.out.println("Deleting expired tokens...");
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
    }

    public static Set<String> getScopesAsSet(String scopes) {
        return Set.of(scopes.split(" "));
    }

    public static OAuthClientAccessType getOauthAccessType(String value) {
        for (OAuthClientAccessType type : OAuthClientAccessType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown access type: " + value);
    }

    public static OAuthResponseType getOauthResponseType(String value) {
        for (OAuthResponseType type : OAuthResponseType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown response type: " + value);
    }


    // Secret length 64 symbols
    public static String generateSecret() {
        byte[] bytes = new byte[32]; // 256 bit
        new SecureRandom().nextBytes(bytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String generateCode() {
        SecureRandom random = new SecureRandom();
        byte[] code = new byte[32];
        random.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }
}
