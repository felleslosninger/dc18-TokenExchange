package com.dc18TokenExchange.STSserver.jwt.exceptions;

public class JWTDecodeException extends JWTVerificationException {
    public JWTDecodeException(String message) {
        this(message, null);
    }

    public JWTDecodeException(String message, Throwable cause) {
        super(message, cause);
    }
}