package com.lostedin.ecosystem.authservice.exception;


public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
