package com.dc18TokenExchange.STSserver.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public ResponseEntity generateToken(Long userId){
        return ResponseEntity.ok().build();
    }
}
