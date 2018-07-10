package com.dc18TokenExchange.OIDCclient;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

@Service
public class TokenValidation {

    public RsaVerifier verifier(String kid, String url) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(url));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    public void verifyClaims(Map claims, String issuer, String clientId) {
        int exp = (int) claims.get("exp");
        Date expireDate = new Date(exp * 1000L);
        Date now = new Date();
        if (!claims.get("iss").equals(issuer) ||
                !claims.get("aud").equals(clientId) || expireDate.before(now)) {
            throw new RuntimeException("Invalid claims");
        }
    }

    public Jwt verifyForCookie(Cookie cookie, String url){
        System.out.println(cookie.getName());
        String at = cookie.getValue();
        try {
            String kid = JwtHelper.headers(at).get("kid");
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(at, verifier(kid, url));
            System.out.println("decoded in verify: \n"+tokenDecoded.toString());
            if(tokenDecoded != null){
                return tokenDecoded;
            }
        } catch (Exception e) {
            //ignore
        }
        return null;
    }
}
