package com.dc18TokenExchange.STSserver;


import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

@Service
public class TokenValidation {

    //Verifies token signature
    public RsaVerifier verifyTokenSignature(String kid, String url) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(url));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    //Verifies token claims
    public void verifyTokenClaims(Map claims, String issuer, String aud) {
        int exp = (int) claims.get("exp");
        Date expireDate = new Date(exp * 1000L);
        Date now = new Date();

        if (!claims.get("iss").equals(issuer) ||
                !claims.get("aud").equals(aud) || //Should be updated to include several clients in the future.
                expireDate.before(now)) {
            throw new RuntimeException("Invalid claims");
        }
    }
}
