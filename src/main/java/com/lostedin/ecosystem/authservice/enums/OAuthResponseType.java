package com.lostedin.ecosystem.authservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OAuthResponseType {

    CODE("code"),
    TOKEN("token"),
    ID_TOKEN("id_token");

    private final String value;

    OAuthResponseType(String value) {
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
