package com.lostedin.ecosystem.authservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OAuthClientType {

    PUBLIC("public"),
    CONFIDENTIAL("confidential");

    private final String value;

    OAuthClientType(String value){
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
