package com.dc18TokenExchange.OIDCclient;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import java.util.Arrays;

@Configuration
@EnableOAuth2Client
public class IdpOpenIdConnectConfig {


    private String clientId = "oidc_dificamp_test";
    private String clientSecret = "a865d76b-0bb2-45b4-92a0-109767da5c7d";
    private String accessTokenUri = "https://oidc-test1.difi.eon.no/idporten-oidc-provider/token";
    private String userAuthorizationUri = "https://oidc-test1.difi.eon.no/idporten-oidc-provider/authorize";
    private String redirectUri = "http://localhost:8000/welcome";

    @Bean
    public OAuth2ProtectedResourceDetails idpOpenId() {

        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();

        System.out.println("Did idpOpenId with" + clientId + clientSecret + accessTokenUri + userAuthorizationUri + redirectUri);

        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(Arrays.asList("openid", "profile"));
        details.setPreEstablishedRedirectUri(redirectUri);
        details.setUseCurrentUri(false);

        return details;
    }

    @Bean
    public OAuth2RestTemplate restTemplate(OAuth2ClientContext clientContext) {
        System.out.println("Did idOpenIdTemplate");
        return new OAuth2RestTemplate(idpOpenId(), clientContext);
    }
}
