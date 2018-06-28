package com.dc18TokenExchange.OIDCclient;

import lombok.Getter;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Getter
@Component
public class IdpOidcClientConfig implements InitializingBean {

    private String clientId = "oidc_dificamp_test";
    private String clientSecret = "673f897e-873a-443a-9aa0-e92541534726";
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

