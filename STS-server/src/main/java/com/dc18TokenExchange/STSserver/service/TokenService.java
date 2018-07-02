package com.dc18TokenExchange.STSserver.service;

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

    public ResponseEntity generateToken(String accessToken) {
        System.out.println(accessToken);
        return ResponseEntity.ok().build();
    }

    public Boolean verifyToken(String token) {
// TODO: Verify token
        Date date = new Date(1478891521000L);
        Clock clock = mock(Clock.class);
        when(clock.getToday()).thenReturn(new Date(DATE_TOKEN_MS_VALUE));
        JWTVerifier.BaseVerification verification = (JWTVerifier.BaseVerification) JWTVerifier.init(Algorithm.HMAC256("secret"))
                .acceptExpiresAt(2)
                .acceptNotBefore(2);
        DecodedJWT jwt =  verification
                .withIssuer("auth0")
                .withSubject("1234567890")
                .withAudience("Mark", "David")
                .withClaim("name", "value")
                .withClaim("name", 123)
                .withClaim("name", 922337203685477600L)
                .withClaim("name", 23.45)
                .withClaim("name", true)
                .withClaim("name", date)
                .withArrayClaim("name", "text", "123", "true")
                .withArrayClaim("name", 1, 2, 3)
                .withJWTId("jwt_id_123")
                .acceptLeeway(1234L)
                .acceptExpiresAt(9999L)
                .acceptNotBefore(9999L)
                .build()
                .verify(token);
        return (jwt==(notNullValue()));
    }
}