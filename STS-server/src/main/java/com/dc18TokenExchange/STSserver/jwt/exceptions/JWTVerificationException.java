package com.dc18TokenExchange.STSserver.jwt.exceptions;

public class JWTVerificationException extends RuntimeException {
    JWTVerificationException(String message) {
        this(message, null);
    }

    JWTVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}