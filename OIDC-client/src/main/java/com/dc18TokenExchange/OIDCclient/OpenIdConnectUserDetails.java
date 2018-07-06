package com.dc18TokenExchange.OIDCclient;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OpenIdConnectUserDetails implements UserDetails {
    private String userId;
    private String username;
    private String workplaceName;
    private int workplaceNum;

    public String at;
    public UsernamePasswordAuthenticationToken upa_token;

    public OpenIdConnectUserDetails(Map<String, Object> userInfo) {
        this.userId = userInfo.get("aud").toString();
        this.username = userInfo.get("pid").toString();

        if(userInfo.containsKey("wrk_num")){
            this.workplaceNum = (int) userInfo.get("wrk_num");
        }
        //else{
        //    throw new IllegalStateException("No orgNum in token");
        //}

        if(userInfo.containsKey("wrk_name")){
            this.workplaceName = userInfo.get("wrk_name").toString();
        }
        //else{
        //    throw new IllegalStateException("No orgName in token");
        //}

    }

    public void set_upa_token(UsernamePasswordAuthenticationToken upa_token){
        this.upa_token = upa_token;
    }

    public UsernamePasswordAuthenticationToken get_upa_token(){
        return upa_token;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(("ROLE_USER")));
    }

    @Override
    public String getPassword() {
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

    public String getAT(){
        return at;
    }

    public void setAT(String at){
        this.at = at;
    }

    public String getWorkplaceName() {
        return workplaceName;
    }

    public void setWorkplaceName(String workplaceName) {
        this.workplaceName = workplaceName;
    }

    public int getWorkplaceNum() {
        return workplaceNum;
    }

    public void setWorkplaceNum(int workplaceNum) {
        this.workplaceNum = workplaceNum;
    }
}
