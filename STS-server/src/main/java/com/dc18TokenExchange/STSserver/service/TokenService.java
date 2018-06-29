package com.dc18TokenExchange.STSserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public ResponseEntity generateToken(String accessToken){
        System.out.println(accessToken);
        return ResponseEntity.ok().build();
    }
}
