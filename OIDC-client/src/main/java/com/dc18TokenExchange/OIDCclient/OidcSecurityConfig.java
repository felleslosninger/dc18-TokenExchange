package com.dc18TokenExchange.OIDCclient;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;

import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableWebSecurity
public class OidcSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuth2RestTemplate restTemplate;

    private String redirectUri = "http://localhost:8000/welcome";


    @Bean
    public OidcFilter openIdConnectFilter() {
        System.out.println("Did openIdConnectFilter");
        OidcFilter filter = new OidcFilter("/loginCredentials");
        filter.setRestTemplate(restTemplate);
        System.out.println("Did setRestTemplate");
        return filter;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .addFilterAfter(new OAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectFilter(), OAuth2ClientContextFilter.class)
                .httpBasic()
                //litt usikker på om det skal brukes redirectUri eller /loginCredentials
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(redirectUri))
                .and()
                .authorizeRequests()
                .antMatchers("/", "/home", "/login", "/welcome", "/accessCode", "/hello", "/logout").permitAll()
                .anyRequest().authenticated();
    }
}
