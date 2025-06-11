package com.lostedin.ecosystem.authservice.model;

import com.lostedin.ecosystem.authservice.exception.InvalidTokenException;
import com.lostedin.ecosystem.authservice.exception.TokenEncryptionException;
import com.lostedin.ecosystem.authservice.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final AbstractNimbus nimbus;

    public JWTUtil(){
        nimbus = new AbstractNimbus(getKey());
    }

    public String generateToken(String subject, long expirationMillis) throws TokenEncryptionException{
        return nimbus.generateJweWithSub(subject,expirationMillis);
    }

    public String generateTokenWithClaims(String subject, Map<String,Object> claims, long expirationMillis) throws TokenEncryptionException {
        return nimbus.generateJweWithSubAndClaims(subject, claims, expirationMillis);
    }

    public void validateToken(String token) throws TokenExpiredException, InvalidTokenException {
        nimbus.validateJwe(token);
    }

    public JWTPayload validateTokenAndGetPayload(String token) throws TokenExpiredException, InvalidTokenException {
        Map<String, Object> claims = nimbus.validateJwe(token);
        return getDecodedJwtFromNimbusClaims(claims);
    }

    private JWTPayload getDecodedJwtFromNimbusClaims(Map<String,Object> claims){

        JWTPayload.JWTPayloadBuilder builder = JWTPayload.builder();
        Map<String, Object> payloadClaims = new HashMap<>();

        claims.forEach((k,v)->{
            switch (k) {
                case "sub" -> builder.subject((String) v);
                case "exp" -> builder.expirationDate((Date) v);
                case "iat" -> builder.issuedAtDate((Date) v);
                default -> payloadClaims.put(k, v);
            }
        });
        if(!payloadClaims.isEmpty()){
            builder.claims(payloadClaims);
        }

        return builder.build();
    }

    private byte[] getKey(){

        //TODO: Not Implemented
        String key = System.getenv("JWE_SHARED_KEY");
        return Base64.getDecoder().decode(key);
    }

}
