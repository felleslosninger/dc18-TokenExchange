package com.dc18TokenExchange.OIDCclient;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Service
public class SetTokenCookie {
    public void setTokenCookie(String tokenName, Map<String, Object> authInfo, String accessToken, HttpServletResponse response){
        int exp = (int) authInfo.get("exp");
        Date expireDate = new Date(exp * 1000L);
        Date now = new Date();

        Cookie cookie = new Cookie(tokenName, accessToken);
        cookie.setMaxAge((int) (expireDate.getTime() - now.getTime()));
        response.addCookie(cookie);
    }
}
