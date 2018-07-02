package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.service.TokenService;
import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class TokenController {

    @Autowired
    TokenService tokenService;


    @PostMapping("/getNewToken")
    public ResponseEntity<String> returnNewToken(@Valid @RequestBody String accessToken) throws IOException, EncoderException {
        System.out.println("got here");

        if(tokenService.verifyToken(accessToken)){
            return tokenService.generateToken(accessToken);
        }
        else{
            return null;
        }
    }
}
