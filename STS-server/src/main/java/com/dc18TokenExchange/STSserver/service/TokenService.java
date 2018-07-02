package com.dc18TokenExchange.STSserver.service;


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
    WorkplaceService workplaceService;

    String token;

    public TokenService() throws NoSuchProviderException, NoSuchAlgorithmException {
    }

    public ResponseEntity<String> generateToken(String accessToken) throws IOException, EncoderException {
        //Recieves and prints the access token.
        String atSubstring = "access_token=";
        accessToken = accessToken.substring(atSubstring.length());
        this.token = accessToken;
        System.out.println("Recieved access token: "+accessToken);

        //Token to Map of diff parts
        Map<String, Object> headerMap = getTokenParts(accessToken, 0);
        Map<String, Object> bodyMap = getTokenParts(accessToken, 1);

        //Gets user ID (personnummer) and checks the authentication resource to find out where he/she works.
        String userWorkplace = getWork(bodyMap, "pid");
        System.out.println(userWorkplace);

        String tokenNew = getNewToken(headerMap, bodyMap,"wrk", userWorkplace);


        System.out.println(tokenNew);


        return new ResponseEntity<>(tokenNew, HttpStatus.OK);
    }

    public Boolean verifyToken(String accessToken){
// TODO: Verify token
        return true;
    }

    private Map<String,Object> getTokenParts(String token, int part) throws IOException {
        String[] split_string = token.split("\\.");
        String base64EncodedPart = split_string[part];
        Base64 base64Url = new Base64(true);
        String stringPart = new String(base64Url.decode(base64EncodedPart));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> partMap = mapper.readValue(stringPart, HashMap.class);
        return partMap;
    }

    private String getWork(Map<String, Object> map, String pid){
        String userPid = map.get(pid).toString();
        Long userPidLong = Long.parseLong(userPid);
        String userWorkplace = workplaceService.getDistinctWorkplaceByUserIdAsString(userPidLong);
        return userWorkplace;
    }

    private String getNewToken(Map<String, Object> header, Map<String, Object> body, String newClaim, Object newClaimValue) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Base64 base64Url = new Base64(true);

        body.put(newClaim, newClaimValue);
        String headerNew = mapper.writeValueAsString(header);
        String bodyNew = mapper.writeValueAsString(body);

        String headerEncoded = new String(base64Url.encode(headerNew.getBytes()));
        String bodyEncoded = new String(base64Url.encode(bodyNew.getBytes()));

        //TODO: Create new signature
       // String sign = getNewSignature(headerEncoded, bodyEncoded);

        //String signatureEncoded = new String(base64Url.encode(sign.getBytes()));


        String tokenNew = headerEncoded + "." + bodyEncoded + "." + getSign(token);
        tokenNew = tokenNew.replaceAll("\\r|\\n", "");
        return tokenNew;
    }


    private String getSign(String token) throws IOException {
        String[] split_string = token.split("\\.");
        String base64EncodedPart = split_string[2];
        return base64EncodedPart;
    }

    private String getNewSignature(String headerEncoded, String bodyEncoded) throws IOException {
        return getSign(token);
    }

}
