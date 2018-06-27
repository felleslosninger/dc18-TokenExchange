package com.dc18TokenExchange.OIDCclient;

import lombok.Getter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Getter
@Component
public class IdpOidcClientConfig implements InitializingBean {

    private String clientId = "oidc_dificamp_test";
    private String clientSecret = "a865d76b-0bb2-45b4-92a0-109767da5c7d";
    private String redirectUri = "http://localhost:8000/welcome";

    private String postLogoutRedirectUri;
    private String logoutUri;

    @Override
    public void afterPropertiesSet() throws Exception {
        logoutUri = redirectUri.replace("welcome", "logut");
        postLogoutRedirectUri = logoutUri + "/response";
    }

    public String getClientId(){
        return clientId;
    }

    public String getRedirectUri(){
        return redirectUri;
    }
}

