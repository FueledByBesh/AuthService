package com.lostedin.ecosystem.authservice.service;

import com.lostedin.ecosystem.authservice.dto.user.UserMinDataDTO;
import com.lostedin.ecosystem.authservice.exception.InvalidTokenException;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.exception.TokenEncryptionException;
import com.lostedin.ecosystem.authservice.exception.TokenExpiredException;
import com.lostedin.ecosystem.authservice.model.JWTPayload;
import com.lostedin.ecosystem.authservice.model.JWTUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class TokenService {

    private final JWTUtil jwtUtil;
    private final Long ACCESS_TOKEN_MILLIS;
    private final Long REFRESH_TOKEN_MILLIS;

    public TokenService(JWTUtil jwtUtil,
                        @Value("${lostedin.config.token.access-token-expire-time-minutes}") int accessTokenExpireTimeMins,
                        @Value("${lostedin.config.token.refresh-token-expire-time-days}") int refreshTokenExpireTimeDays) {
        this.jwtUtil = jwtUtil;
        this.ACCESS_TOKEN_MILLIS = accessTokenExpireTimeMins * 60_000L;
        this.REFRESH_TOKEN_MILLIS = refreshTokenExpireTimeDays * 24 * 60 * 60 * 1000L;
    }

    public String createAccessTokenWithClaims(String sub, Map<String, Object> claims ){
        try {
            return jwtUtil.generateTokenWithClaims(sub, claims, ACCESS_TOKEN_MILLIS);
        }catch (TokenEncryptionException e){
            throw new ServiceException(500, "Internal Server Error while creating Access Token: " + e.getMessage());
        }
    }

    public String refreshAccessToken(String refreshToken){
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

    public void deleteRefreshTokenByUserId(UUID userId){
        //TODO: Not Implemented
    }

    public String createRefreshToken( String sub, Map<String, Object> claims){
        try {
            return jwtUtil.generateTokenWithClaims(sub, claims, REFRESH_TOKEN_MILLIS);
        }catch (TokenEncryptionException e){
            throw new ServiceException(500, "Internal Server Error while creating Refresh Token: " + e.getMessage());
        }
    }

    public void deleteExpiredTokens(){}

    public void deleteTokensByUserId(){}

    public void deleteTokensByClientId(){}

    public void deleteTokensByUserIdAndClientId(){}

    public void createIdToken(UserMinDataDTO user){}


}
