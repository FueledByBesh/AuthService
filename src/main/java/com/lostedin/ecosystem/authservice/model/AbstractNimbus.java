package com.lostedin.ecosystem.authservice.model;


import com.lostedin.ecosystem.authservice.exception.InvalidTokenException;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import com.lostedin.ecosystem.authservice.exception.TokenEncryptionException;
import com.lostedin.ecosystem.authservice.exception.TokenExpiredException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class AbstractNimbus {

    private final byte[] key;

    public AbstractNimbus(byte[] key){
        this.key = key;
    }

    public String generateJweWithSub(String sub, long expirationMillis) throws TokenEncryptionException {

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(sub)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationMillis))
                .build();

        JWEHeader header = new JWEHeader.Builder(
                JWEAlgorithm.DIR,
                EncryptionMethod.A256GCM)
                .contentType("JWT")
                .build();

        EncryptedJWT jwt = new EncryptedJWT(header, claimsSet);
        encryptJweDirect(jwt);

        return jwt.serialize();
    }

    public String generateJweWithSubAndClaims(String sub, Map<String, Object> claims, long expirationMillis) throws TokenEncryptionException {

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(sub)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationMillis));

        claims.forEach(builder::claim);
        JWTClaimsSet claimsSet = builder.build();

        JWEHeader header = new JWEHeader.Builder(
                JWEAlgorithm.DIR,
                EncryptionMethod.A256GCM)
                .contentType("JWT")
                .build();

        EncryptedJWT jwt = new EncryptedJWT(header, claimsSet);
        encryptJweDirect(jwt);

        return jwt.serialize();
    }

    public Map<String,Object> validateJwe(String stringJwe) throws TokenExpiredException, InvalidTokenException {
        EncryptedJWT decryptedJwe = decryptJweDirect(stringJwe);
        JWTClaimsSet claimsSet = getClaimsSet(decryptedJwe);
        if(claimsSet.getExpirationTime().before(new Date())){
            throw new TokenExpiredException("Token expired");
        }
        return claimsSet.getClaims();
    }


    private JWTClaimsSet getClaimsSet(EncryptedJWT jwe){
        JWTClaimsSet claims;
        try {
            claims = jwe.getJWTClaimsSet();
        }catch (ParseException e){
            throw new InvalidTokenException("Invalid structure of payload");
        }
        return claims;
    }



    private EncryptedJWT decryptJweDirect(String stringJwe) throws InvalidTokenException {
        EncryptedJWT jwe;
        try {
            jwe = EncryptedJWT.parse(stringJwe);
            DirectDecrypter decrypter = new DirectDecrypter(key);
            jwe.decrypt(decrypter);
        }catch (ParseException | JOSEException e){
            throw new InvalidTokenException("Invalid Token");
        }
        if(jwe.getState() != JWEObject.State.DECRYPTED){
            throw new InvalidTokenException("Invalid Token");
        }
        return jwe;
    }


    private void encryptJweDirect(EncryptedJWT jwt) throws TokenEncryptionException {
        try {
            DirectEncrypter encrypter = new DirectEncrypter(key);
            jwt.encrypt(encrypter);
        }catch (JOSEException e){
            throw new TokenEncryptionException("Couldn't encrypt token");
        }
        if(jwt.getState() != JWEObject.State.ENCRYPTED){
            throw new TokenEncryptionException("Couldn't encrypt token");
        }
    }


}
