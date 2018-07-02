package com.dc18TokenExchange.STSserver.service;

import com.dc18TokenExchange.STSserver.jwt.JWTCreator;
import com.dc18TokenExchange.STSserver.jwt.JWTDecoder;
import com.dc18TokenExchange.STSserver.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Autowired
    WorkplaceService workplaceService;

    public ResponseEntity<String> generateToken(String accessToken) throws IOException, EncoderException {
        //Recieves and prints the access token.
        String atSubstring = "access_token=";
        accessToken = accessToken.substring(atSubstring.length());
        System.out.println("Recieved access token: "+accessToken);

        //Decodes token and splits into header and body
        String[] split_string = accessToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        System.out.println("JWT Header : " + header);

        String body = new String(base64Url.decode(base64EncodedBody));
        System.out.println("JWT Body : "+body);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> headerMap = mapper.readValue(header, HashMap.class);
        Map<String, Object> bodyMap = mapper.readValue(body, HashMap.class);


        //Gets user ID (personnummer) and checks the authentication resource to find out where he/she works.
        String userPid = bodyMap.get("pid").toString();
        Long userPidLong = Long.parseLong(userPid);
        String userWorkplace = workplaceService.getDistinctWorkplaceByUserIdAsString(userPidLong);
        System.out.println(userWorkplace);

        //Puts the new claim into the body-map and generates a new token with the new claim.
        bodyMap.put("wrk", userWorkplace);
        String headerNew = mapper.writeValueAsString(headerMap);
        String bodyNew = mapper.writeValueAsString(bodyMap);
        //String headerNew = headerMap.toString();
        //String bodyNew = bodyMap.toString();
        //TODO: Create new signature
        System.out.println(headerNew);
        System.out.println(bodyNew);

        String headerEncoded = new String(base64Url.encode(headerNew.getBytes()));
        String bodyEncoded = new String(base64Url.encode(bodyNew.getBytes()));
        //String signatureEncoded = base64EncodedSignature;  Insert new signature here

        String tokenNew = headerEncoded + "." + bodyEncoded + "." + base64EncodedSignature;
        tokenNew = tokenNew.replaceAll("\\r|\\n", "");

        System.out.println(tokenNew);


        /*Code to verify and decode the claims of the JWT
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey("bleh")
                .parseClaimsJws(accessToken);

        System.out.println("Header: " + claims.getHeader());
        System.out.println("Body: " + claims.getBody());
        System.out.println("Signature: " + claims.getSignature());*/

        return new ResponseEntity<>(tokenNew, HttpStatus.OK);
    }

    public Boolean verifyToken(String accessToken){
// TODO: Verify token
        return true;
    }
}
