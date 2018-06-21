package com.dc18TokenExchange.OIDCclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class OpenIdConnectFilter extends AbstractAuthenticationProcessingFilter {

    public OpenIdConnectFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(new NoopAuthenticationManager());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken accessToken;

        //Gets access token and returns exception if it is not present
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            throw new BadCredentialsException("Could not obtain access token", e);
        }

        //Gets claims from id-token and returns exception if they are not present
        try {
            String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
            String kid = JwtHelper.headers(idToken).get("kid");

            Jwt tokenDecoded = JwtHelper.decodeAndVerify(idToken, verifier(kid));

            Map<String, String> authInfo = new ObjectMapper();
            authInfo.readValue(tokenDecoded.getClaims(), Map.class);

            verifyClaims(authInfo);
            OpenIdConnectUserDetails user = new OpenIdConnectUserDetails(authInfo, accessToken);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } catch (InvalidTokenException e) {
            throw new BadCredentialsException("Could not obtain user details from token", e);
        }
    }
}
