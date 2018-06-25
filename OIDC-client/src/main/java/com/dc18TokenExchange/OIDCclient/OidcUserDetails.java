package com.dc18TokenExchange.OIDCclient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collection;
import java.util.Map;

public class OidcUserDetails {
    private String userId;
    private String pId;
    private OAuth2AccessToken token;

    public OidcUserDetails(Map<String, String> userInfo, OAuth2AccessToken token) {
        this.userId = userInfo.get("kid");
        this.pId = userInfo.get("pId");
        this.token = token;
    }

    public void setpId(String pId){
        this.pId = pId;
    }

    public String getpId(){
        return pId;
    }

    public void setUserId(String kid){
        this.userId = kid;
    }

    public String getUserId(){
        return userId;
    }
}
