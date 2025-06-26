package com.lostedin.ecosystem.authservice.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusType {

    ACTIVE("active"),
    INACTIVE("inactive"),
    BANNED("banned");

    private final String value;

    StatusType(String value) {
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
