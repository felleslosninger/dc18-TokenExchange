package com.dc18TokenExchange.STSserver.jwt.exceptions;

public class JWTCreationException extends RuntimeException {
    public JWTCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
