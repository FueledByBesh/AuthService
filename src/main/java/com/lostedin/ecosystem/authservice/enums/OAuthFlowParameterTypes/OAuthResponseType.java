package com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OAuthResponseType {

    CODE("code");

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
