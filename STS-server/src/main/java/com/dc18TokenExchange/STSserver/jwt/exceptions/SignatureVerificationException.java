package com.dc18TokenExchange.STSserver.jwt.exceptions;

import com.dc18TokenExchange.STSserver.jwt.algorithms.Algorithm;

public class SignatureVerificationException extends JWTVerificationException {
    public SignatureVerificationException(Algorithm algorithm) {
        this(algorithm, null);
    }

    public SignatureVerificationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature resulted invalid when verified using the Algorithm: " + algorithm, cause);
    }
}