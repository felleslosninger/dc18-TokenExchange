package com.dc18TokenExchange.STSserver.jwt.interfaces;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA Public/Private Key provider.
 */
public interface RSAKeyProvider extends KeyProvider<RSAPublicKey, RSAPrivateKey> {
}
