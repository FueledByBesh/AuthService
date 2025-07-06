package com.lostedin.ecosystem.authservice.enums.OAuthFlowParameterTypes;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CodeChallengeMethodType {

    S256("S256"),
    PLAIN("plain");

    private final String value;

    CodeChallengeMethodType(String value) {
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
