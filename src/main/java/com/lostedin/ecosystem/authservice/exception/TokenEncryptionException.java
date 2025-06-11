package com.lostedin.ecosystem.authservice.exception;

public class TokenEncryptionException extends RuntimeException {
  public TokenEncryptionException(String message) {
    super(message);
  }
}
