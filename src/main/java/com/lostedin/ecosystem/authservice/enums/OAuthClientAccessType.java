package com.lostedin.ecosystem.authservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OAuthClientAccessType {

    PUBLIC("public"),
    TRUSTED("trusted"),
    CONFIDENTIAL("confidential");

    private final String value;

    OAuthClientAccessType(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
