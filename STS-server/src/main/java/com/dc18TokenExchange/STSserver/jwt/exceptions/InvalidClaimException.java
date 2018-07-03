package com.dc18TokenExchange.STSserver.jwt.exceptions;

public class InvalidClaimException extends JWTVerificationException {
    public InvalidClaimException(String message) {
        super(message);
    }
}