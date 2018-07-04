package com.dc18TokenExchange.STSserver.service;


import com.auth0.jwk.InvalidPublicKeyException;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.dc18TokenExchange.STSserver.TokenGenerator;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public static final long DATE_TOKEN_MS_VALUE = 1477592 * 1000;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Value("${approved.idp.provider}")
    private String provider;

    @Autowired
    TokenGenerator tokenGenerator;


    public ResponseEntity<String> generateToken(String accessToken) throws IOException, EncoderException {
        //Recieves and prints the access token.
        String atSubstring = "access_token=";
        accessToken = accessToken.substring(atSubstring.length());
        System.out.println("Recieved access token: " + accessToken);

        //Token to Map of diff parts
        Map<String, Object> headerMap = tokenGenerator.getTokenParts(accessToken, 0);
        Map<String, Object> bodyMap = tokenGenerator.getTokenParts(accessToken, 1);

        //Gets user ID (personnummer) and checks the authentication resource to find out where he/she works.
        String userWorkplace = tokenGenerator.getWork(bodyMap, "pid");
        System.out.println(userWorkplace);

        String tokenNew = tokenGenerator.getNewToken(headerMap, bodyMap, "wrk", userWorkplace);

        System.out.println("New token: " + tokenNew);

        return new ResponseEntity<>(tokenNew, HttpStatus.OK);
    }

    public RsaVerifier verifyTokenSignature(String kid, String url) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(url));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    public Boolean verifyTokenClaims(String token) {

        Date date = new Date(1478891521000L);
        Clock clock = mock(Clock.class);
        when(clock.getToday()).thenReturn(new Date(DATE_TOKEN_MS_VALUE));

        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWTVerifier.init(Algorithm.HMAC256("secret"))
                .acceptExpiresAt(2)
                .acceptNotBefore(2);

        DecodedJWT jwt = verification
                .withIssuer(provider) //This should be able to check multiple token providers in the future
                .withSubject("GjCeg75NQyEzXsjM6YSX-lLCuB6iEXZFztn01SeD4ts=")
                .withAudience("oidc_dificamp_test") //This should be able to verify different clients later in case several clients utilize the STS
                .acceptLeeway(1234L)
                .acceptExpiresAt(9999L)
                .acceptNotBefore(9999L)
                .build()
                .verify(token);
        return (jwt==(notNullValue()));
    }
}