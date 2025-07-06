package com.lostedin.ecosystem.authservice.model;

import com.lostedin.ecosystem.authservice.enums.OAuthClientType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.CodeChallengeMethodType;
import com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes.OAuthResponseType;
import com.lostedin.ecosystem.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
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

    public static OAuthClientType getOauthClientType(String value) throws IllegalArgumentException{
        for (OAuthClientType type : OAuthClientType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown client type: " + value);
    }

    public static OAuthResponseType getOauthResponseType(String value) throws IllegalArgumentException {
        for (OAuthResponseType type : OAuthResponseType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown response type: " + value);
    }

    public static CodeChallengeMethodType getCodeChallengeMethodType(String value) throws IllegalArgumentException {
        for (CodeChallengeMethodType type : CodeChallengeMethodType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown code challenge method type: " + value);
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

    public static String generateRandomBase64UrlEncodedString(int byteLength) {
        byte[] randomBytes = new byte[byteLength];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }


    // returns 64 symbols of a hashed input string
    public static String hashString(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] inputHashed = md.digest(input.getBytes(StandardCharsets.UTF_8)); // 32 byte hashed string
        return HexFormat.of().formatHex(inputHashed); // 64 symbols from 32 bytes
    }

}
