package com.dc18TokenExchange.STSserver.jwt.algorithms;

import com.dc18TokenExchange.STSserver.jwt.exceptions.SignatureGenerationException;
import com.dc18TokenExchange.STSserver.jwt.exceptions.SignatureVerificationException;
import com.dc18TokenExchange.STSserver.jwt.interfaces.DecodedJWT;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class HMACAlgorithm extends Algorithm {

    private final CryptoHelper crypto;
    private final byte[] secret;

    //Visible for testing
    HMACAlgorithm(CryptoHelper crypto, String id, String algorithm, byte[] secretBytes) throws IllegalArgumentException {
        super(id, algorithm);
        if (secretBytes == null) {
            throw new IllegalArgumentException("The Secret cannot be null");
        }
        this.secret = secretBytes;
        this.crypto = crypto;
    }

    HMACAlgorithm(String id, String algorithm, byte[] secretBytes) throws IllegalArgumentException {
        this(new CryptoHelper(), id, algorithm, secretBytes);
    }

    HMACAlgorithm(String id, String algorithm, String secret) throws IllegalArgumentException {
        this(new CryptoHelper(), id, algorithm, getSecretBytes(secret));
    }

    //Visible for testing
    static byte[] getSecretBytes(String secret) throws IllegalArgumentException {
        if (secret == null) {
            throw new IllegalArgumentException("The Secret cannot be null");
        }
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public void verify(DecodedJWT jwt) throws SignatureVerificationException {
        byte[] contentBytes = String.format("%s.%s", jwt.getHeader(), jwt.getPayload()).getBytes(StandardCharsets.UTF_8);
        byte[] signatureBytes = Base64.decodeBase64(jwt.getSignature());

        try {
            boolean valid = crypto.verifySignatureFor(getDescription(), secret, contentBytes, signatureBytes);
            if (!valid) {
                throw new SignatureVerificationException(this);
            }
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new SignatureVerificationException(this, e);
        }
    }

    @Override
    public byte[] sign(byte[] contentBytes) throws SignatureGenerationException {
        try {
            return crypto.createSignatureFor(getDescription(), secret, contentBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureGenerationException(this, e);
        }
    }

}