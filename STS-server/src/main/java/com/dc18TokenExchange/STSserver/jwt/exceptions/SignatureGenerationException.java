package com.dc18TokenExchange.STSserver.jwt.exceptions;

import com.dc18TokenExchange.STSserver.jwt.algorithms.Algorithm;

public class SignatureGenerationException extends JWTCreationException {
    public SignatureGenerationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature couldn't be generated when signing using the Algorithm: " + algorithm, cause);
    }
}