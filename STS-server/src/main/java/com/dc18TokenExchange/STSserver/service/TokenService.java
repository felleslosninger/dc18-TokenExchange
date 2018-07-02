package com.dc18TokenExchange.STSserver.service;


import com.dc18TokenExchange.STSserver.TokenGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;

import org.bouncycastle.jcajce.provider.keystore.BC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;


@Service
public class TokenService {

    @Autowired
    TokenGenerator tokenGenerator;


    public ResponseEntity<String> generateToken(String accessToken) throws IOException, EncoderException {
        //Recieves and prints the access token.
        String atSubstring = "access_token=";
        accessToken = accessToken.substring(atSubstring.length());
        System.out.println("Recieved access token: "+accessToken);

        //Token to Map of diff parts
        Map<String, Object> headerMap = tokenGenerator.getTokenParts(accessToken, 0);
        Map<String, Object> bodyMap = tokenGenerator.getTokenParts(accessToken, 1);

        //Gets user ID (personnummer) and checks the authentication resource to find out where he/she works.
        String userWorkplace = tokenGenerator.getWork(bodyMap, "pid");
        System.out.println(userWorkplace);

        String tokenNew = tokenGenerator.getNewToken(headerMap, bodyMap,"wrk", userWorkplace);

        System.out.println("New token: " + tokenNew);

        return new ResponseEntity<>(tokenNew, HttpStatus.OK);
    }

    public Boolean verifyToken(String accessToken){
// TODO: Verify token
        return true;
    }
}
