package com.dc18TokenExchange.STSserver.service;


import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.dc18TokenExchange.STSserver.TokenGenerator;
import com.dc18TokenExchange.STSserver.jwt.interfaces.RSAKeyProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import com.dc18TokenExchange.STSserver.jwt.JWTVerifier;
import com.dc18TokenExchange.STSserver.jwt.algorithms.Algorithm;
import com.dc18TokenExchange.STSserver.jwt.interfaces.Clock;
import com.dc18TokenExchange.STSserver.jwt.interfaces.DecodedJWT;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Service
public class TokenService {

    @Value("${approved.idp.issuer}")
    private String issuer;

    @Value("${approved.idp.jwk}")
    private String jwkUrl;

    @Value("${approved.aud}")
    private String aud;

    @Autowired
    TokenGenerator tokenGenerator;


    public ResponseEntity<String> generateToken(String accessToken) throws Exception {
        //Recieves and prints the access token.
        String atSubstring = "access_token=";
        accessToken = accessToken.substring(atSubstring.length());
        System.out.println("Recieved access token: " + accessToken);

        //Verifies token signature
        String kid = JwtHelper.headers(accessToken).get("kid");
        Jwt tokenDecoded = JwtHelper.decodeAndVerify(accessToken, verifyTokenSignature(kid, jwkUrl));

        //Verifies token claims
        Map<String, Object> authInfo = new ObjectMapper()
                .readValue(tokenDecoded.getClaims(), Map.class);
        System.out.println(authInfo.get("aud"));
        System.out.println(authInfo.get("iss"));
        verifyTokenClaims(authInfo);

        //Token to Map of diff parts
        Map<String, Object> headerMap = tokenGenerator.getTokenParts(accessToken, 0);
        Map<String, Object> bodyMap = tokenGenerator.getTokenParts(accessToken, 1);

        //Gets user ID (personnummer) and checks the authentication resource to find out where he/she works.
        String userWorkplace = tokenGenerator.getWork(bodyMap, "pid");
        System.out.println(userWorkplace);

        //Generates the token
        String tokenNew = tokenGenerator.getNewToken(headerMap, bodyMap, "wrk", userWorkplace);
        System.out.println("New token: " + tokenNew);

        return new ResponseEntity<>(tokenNew, HttpStatus.OK);
    }

    public RsaVerifier verifyTokenSignature(String kid, String url) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(url));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    public void verifyTokenClaims(Map claims) {
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