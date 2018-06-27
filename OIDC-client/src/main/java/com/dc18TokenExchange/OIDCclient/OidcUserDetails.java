package com.dc18TokenExchange.OIDCclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


@Data
@Builder
@AllArgsConstructor

public class OidcUserDetails implements UserDetails {
    private String sub;
    private String pId;
    private String token;


    public OidcUserDetails(Map<String, String> userInfo, String token) {
        this.sub = userInfo.get("sub");
        this.pId = userInfo.get("pId");
        this.token = token;
    }
    public  OidcUserDetails(){
        this(null,null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.pId;
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



}
