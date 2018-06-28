package com.dc18TokenExchange.STSserver.jwt.exceptions;

public class AlgorithmMismatchException extends JWTVerificationException {
    public AlgorithmMismatchException(String message) {
        super(message);
    }
}
