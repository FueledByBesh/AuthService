package com.lostedin.ecosystem.authservice.model;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.lostedin.ecosystem.authservice.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

//@Deprecated(forRemoval = true)
@Component
public class JwtShit {

    private final RSAKeyProvider rsaKeyProvider;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public JwtShit(RSAKeyProvider rsaKeyProvider) {
        this.rsaKeyProvider = rsaKeyProvider;
//        this.privateKey = RSAKeyConverter.convertPrivateKey(rsaKeyProvider.getPrivateKey());
//        this.publicKey = RSAKeyConverter.convertPublicKey(rsaKeyProvider.getPublicKey());
    }

    public String generateUserToken(String idAsSub, String username, long expirationMillis) {
        return JWT.create()
                .withSubject(idAsSub)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(Algorithm.RSA256(null, privateKey));
    }

    public String generateServiceToken(String serviceName, long expirationMillis) {
        return JWT.create()
                .withSubject(serviceName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(Algorithm.RSA256(null, privateKey));
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT jwt = this.getJwt(token);
            return jwt.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            return false;
        }

    }

    private DecodedJWT getJwt(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.RSA256(publicKey, null)).build();
        return verifier.verify(token);
    }

    public Map<String, String> getSubAndClaims(String token) {
        try {
            DecodedJWT jwt = this.getJwt(token);
            return Map.of(
                    "sub", jwt.getSubject(),
                    "username", jwt.getClaim("username").asString()
            );
        } catch (JWTVerificationException e) {
            throw new ServiceException(401, "Invalid token");
        }
    }

}
