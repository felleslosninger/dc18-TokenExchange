package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.service.TokenService;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class TokenController {

    @Value("${approved.idp.jwk}")
    private String jwkUrl;

    @Autowired
    TokenService tokenService;


    @PostMapping("/getNewToken")
    public ResponseEntity<String> returnNewToken(@Valid @RequestBody String accessToken) throws Exception {
        System.out.println("got here");

        String kid = JwtHelper.headers(accessToken).get("kid");
        Jwt tokenDecoded = JwtHelper.decodeAndVerify(accessToken, tokenService.verifyTokenSignature(kid, jwkUrl));

        return tokenService.generateToken(accessToken);

    }
}
