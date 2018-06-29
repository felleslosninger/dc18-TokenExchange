package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    TokenService tokenService;


    @GetMapping("/getNewToken")
    public ResponseEntity returnNewToken(Long userId){
        return tokenService.generateToken(userId);
    }
}
