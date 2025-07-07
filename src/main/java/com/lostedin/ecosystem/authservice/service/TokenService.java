package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.user.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.entity.RefreshTokenEntity;
import com.lostedin.ecosystem.authservice.entity.SessionEntity;
import com.lostedin.ecosystem.authservice.exception.InvalidTokenException;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.exception.TokenEncryptionException;
import com.lostedin.ecosystem.authservice.exception.TokenExpiredException;
import com.lostedin.ecosystem.authservice.model.Helper;
import com.lostedin.ecosystem.authservice.model.JWTPayload;
import com.lostedin.ecosystem.authservice.model.JWTUtil;
import com.lostedin.ecosystem.authservice.repository.RefreshTokenRepository;
import com.lostedin.ecosystem.authservice.repository.SessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {

    private final RefreshTokenRepository rtRepository;

    private final JWTUtil jwtUtil;
    private final Long ACCESS_TOKEN_MILLIS;
    private final Long REFRESH_TOKEN_MILLIS;

    public TokenService(JWTUtil jwtUtil,
                        @Value("${lostedin.config.token.access-token-expire-time-minutes}") int accessTokenExpireTimeMins,
                        @Value("${lostedin.config.token.refresh-token-expire-time-days}") int refreshTokenExpireTimeDays,
                        RefreshTokenRepository rtRepository) {
        this.jwtUtil = jwtUtil;
        this.ACCESS_TOKEN_MILLIS = accessTokenExpireTimeMins * 60_000L;
        this.REFRESH_TOKEN_MILLIS = refreshTokenExpireTimeDays * 24 * 60 * 60 * 1000L;
        this.rtRepository = rtRepository;
    }

    public String createAccessTokenWithClaims(String sub, Map<String, Object> claims ){
        try {
            return jwtUtil.generateTokenWithClaims(sub, claims, ACCESS_TOKEN_MILLIS);
        }catch (TokenEncryptionException e){
            throw new ServiceException(500, "Internal Server Error while creating Access Token: " + e.getMessage());
        }
    }

    public String refreshAccessToken(String refreshToken){

        // TODO: 03.07.2025 , Status: Not Implemented
        //  1) Should validate refresh token (Check whether it exists or not in database)
        //  2) Get user id from sessions, not from inside refresh token, because refresh token should be opaque

        try {
            JWTPayload payload = jwtUtil.validateTokenAndGetPayload(refreshToken);
            if(Objects.isNull(payload.getClaims()) || payload.getClaims().isEmpty()) {
                return jwtUtil.generateToken(payload.getSubject(), ACCESS_TOKEN_MILLIS);
            }

            return jwtUtil.generateTokenWithClaims(
                    payload.getSubject(),
                    payload.getClaims(),
                    ACCESS_TOKEN_MILLIS
            );
        }catch (TokenExpiredException | InvalidTokenException e){
            throw new ServiceException(401, "Bad Token: " + e.getMessage());
        }
    }

    public void deleteRefreshTokenByToken(String token){
        //TODO: Not Implemented
    }
    public void deleteRefreshTokenByClientId(UUID clientId){
        //TODO: Not Implemented
    }


    public String createRefreshToken(SessionEntity session){
        try {
            String token = generateOpaqueToken();
            RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
            refreshTokenEntity.setTokenHash(hashToken(token));
            refreshTokenEntity.setTokenTTLMillis(REFRESH_TOKEN_MILLIS);
            refreshTokenEntity.setSession(session);
            rtRepository.saveAndFlush(refreshTokenEntity);
            return token;
        }catch (Exception e){
            log.error("Couldn't create refresh token", e);
            throw new ServiceException(500,"Internal Server Error: Server couldn't create refresh token");
        }
    }

    public void deleteExpiredRefreshTokens(){
        // TODO: Not Implemented
    }

    public void deleteTokensByUserId(){}

    public void deleteTokensByClientId(){}

    public void deleteTokensByUserIdAndClientId(){}

    public void createIdToken(UserMinDataDTO user){}

    private String generateOpaqueToken(){
        try {
            return Helper.generateRandomBase64UrlEncodedString(64);
        }catch (Exception e){
            log.error("Couldn't generate token", e);
            throw new ServiceException(500,"Internal Server Error: Server couldn't generate token");
        }
    }

    private String hashToken(String token){
        try {
            return Helper.hashString(token);
        }catch (Exception e){
            log.error("Couldn't hash token", e);
            throw new ServiceException(500,"Internal Server Error: Server couldn't hash token");
        }
    }

}
