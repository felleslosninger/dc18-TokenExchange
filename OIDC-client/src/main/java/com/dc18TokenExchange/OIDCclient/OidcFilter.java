package com.dc18TokenExchange.OIDCclient;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

public class OidcFilter extends AbstractAuthenticationProcessingFilter {

    public OidcFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(new NoopAuthenticationManager());
    }

    private OAuth2RestTemplate restTemplate;

    public void setRestTemplate(OAuth2RestTemplate template){
        this.restTemplate = template;
    }

    //Used values in class
    @Value("${idp.jwkUrl}")
    private String jwkUrl;

    @Value("{idp.clientId}")
    private String clientId;

    @Value("{idp.issuer}")
    private String issuer;

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
            //Turns ID token into string and finds the kid number
            String idToken = accessToken.getAdditionalInformation().get("id_token").toString();
            String kid = JwtHelper.headers(idToken).get("kid");

            //Verifies token signature and kid number
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(idToken, verifier(kid));

            //Maps token into string
            ObjectMapper om = new ObjectMapper();
            //Map<String, String> authInfo = new ObjectMapper();
            Map<String, String> authInfo = om.readValue(tokenDecoded.getClaims(), Map.class);

            //Verifies claims matching
            verifyClaims(authInfo);

            //Creates new user and adds it to a new authentication token
            OidcUserDetails user = new OidcUserDetails(authInfo, accessToken);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } catch (InvalidTokenException e) {
            throw new BadCredentialsException("Could not obtain user details from token", e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private RsaVerifier verifier(String kid) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(jwkUrl));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    public void verifyClaims(Map claims) {
        int exp = (int) claims.get("exp");
        Date expireDate = new Date(exp * 1000L);
        Date now = new Date();
        if (expireDate.before(now) || !claims.get("iss").equals(issuer) ||
                !claims.get("aud").equals(clientId)) {
            throw new RuntimeException("Invalid claims");
        }
    }
}