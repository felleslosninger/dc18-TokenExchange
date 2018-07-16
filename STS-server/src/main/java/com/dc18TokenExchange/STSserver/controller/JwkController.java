package com.dc18TokenExchange.STSserver.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class JwkController {
    @Value("${jwks.kty}")
    private String kty;
    @Value("${jwks.e}")
    private String e;
    @Value("${jwks.use}")
    private String use;
    @Value("${jwks.kid}")
    private String kid;
    @Value("${jwks.n}")
    private String n;
    @Value("${jwks.x5c}")
    private String x5c;
    @Value("${jwks.alg}")
    private String alg;
    @Value("${jwks.x5t}")
    private String x5t;

    @GetMapping("/jwk")
    public String returnJwk() {
        List<JSONObject> keysList = new ArrayList<>();
        List<String> x5cList = new ArrayList<>();
        x5cList.add(x5c);
        keysList.add(new JSONObject()
                .put("kty", kty)
                .put("e", e)
                .put("use", use)
                .put("kid", kid)
                .put("n", n)
                .put("x5c", x5cList)
                .put("alg", alg)
                .put("x5t", x5t));
        return new JSONObject().put("keys", keysList).toString();
    }
}
