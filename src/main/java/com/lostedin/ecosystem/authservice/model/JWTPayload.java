package com.lostedin.ecosystem.authservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Builder
public class JWTPayload {

    private Date expirationDate;
    private Date issuedAtDate;
    private String subject;
    private Map<String, Object> claims;

}

