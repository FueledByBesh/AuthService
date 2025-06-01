package com.lostedin.ecosystem.authservice.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class RSAKeyProvider {

    @Value("${JWT_PRIVATE_KEY}")
    private String privateKeyBase64;

    @Value("${JWT_PUBLIC_KEY}")
    private String publicKeyBase64;

    public String getPrivateKey() {
        return new String(Base64.getDecoder().decode(privateKeyBase64));
    }

    public String getPublicKey() {
        return new String(Base64.getDecoder().decode(publicKeyBase64));
    }

}
