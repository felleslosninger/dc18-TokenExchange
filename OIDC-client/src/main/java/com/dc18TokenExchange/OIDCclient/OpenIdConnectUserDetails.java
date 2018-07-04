package com.dc18TokenExchange.OIDCclient;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class OpenIdConnectUserDetails implements UserDetails {
    private String userId;
    private String username;
    private OAuth2AccessToken token;
    public static String at;
    public static UsernamePasswordAuthenticationToken uttoken;

    public OpenIdConnectUserDetails(Map<String, Object> userInfo) {
        this.userId = userInfo.get("aud").toString();
        this.username = userInfo.get("pid").toString();
    }

    public void setOautToken(OAuth2AccessToken token){
        this.token = token;
    }

    public void setUTToken(UsernamePasswordAuthenticationToken uttoken){
        System.out.println("Er inne i setting uttoken");
        this.uttoken = uttoken;
    }

    public static UsernamePasswordAuthenticationToken getUTToken(){
        return uttoken;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(("ROLE_USER")));
    }

    @Override
    public  String getPassword() {
        return at;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static String getAT(){
        return at;
    }

    public void setAT(String at){
        this.at = at;
    }
}
