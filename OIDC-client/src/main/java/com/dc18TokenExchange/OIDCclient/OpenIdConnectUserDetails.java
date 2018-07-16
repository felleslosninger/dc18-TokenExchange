package com.dc18TokenExchange.OIDCclient;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class OpenIdConnectUserDetails implements UserDetails {
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String workplaceName;
    private int workplaceNum;
    private String at;
    private UsernamePasswordAuthenticationToken upa_token;

    OpenIdConnectUserDetails(Map<String, Object> userInfo) {
        this.userId = userInfo.get("pid").toString();
        if (userInfo.containsKey("wrk_num")) {
            this.workplaceNum = (int) userInfo.get("wrk_num");
        } else {
            throw new IllegalStateException("No orgNum in token");
        }
        if (userInfo.containsKey("wrk_name")) {
            this.workplaceName = userInfo.get("wrk_name").toString();
        } else {
            throw new IllegalStateException("No orgName in token");
        }
        if (userInfo.containsKey("f_name")) {
            this.firstName = userInfo.get("f_name").toString();
        } else {
            throw new IllegalStateException("No first name in token");
        }
        if (userInfo.containsKey("l_name")) {
            this.lastName = userInfo.get("l_name").toString();
        } else {
            throw new IllegalStateException("No last name in token");
        }
    }

    UsernamePasswordAuthenticationToken get_upa_token() {
        return upa_token;
    }

    void set_upa_token(UsernamePasswordAuthenticationToken upa_token) {
        this.upa_token = upa_token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(("ROLE_USER")));
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

    public String getAT() {
        return at;
    }

    public void setAT(String at) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first_name) {
        this.firstName = first_name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }
}
