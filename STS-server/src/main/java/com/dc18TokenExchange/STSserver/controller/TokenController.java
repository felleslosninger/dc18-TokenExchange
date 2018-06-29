package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TokenController {

    @Autowired
    TokenService tokenService;


    @PostMapping("/getNewToken")
    public ResponseEntity returnNewToken(@Valid @RequestBody String accessToken){
        return tokenService.generateToken(accessToken);
    }
}
