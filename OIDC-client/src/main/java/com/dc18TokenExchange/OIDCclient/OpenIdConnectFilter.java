package com.dc18TokenExchange.OIDCclient;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.Key;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@PropertySource("classpath:application.properties")
public class OpenIdConnectFilter extends AbstractAuthenticationProcessingFilter {

    //Below values are for the IDP connection
    @Value("${idp.clientId}")
    private String clientId;

    @Value("${idp.clientSecret}")
    private String clientSecret;

    @Value("${idp.issuer}")
    private String issuer;

    @Value("${idp.jwkUrl}")
    private String jwkUrl;

    //Below values are for the STS connection
    @Value("${sts.username}")
    private String sts_username;

    @Value("${sts.password}")
    private String sts_password;

    @Value("${sts.url}")
    private String sts_url;

    @Value("${sts.jwks}")
    private String sts_jwks;


    @Autowired
    TokenValidation tokenValidation;

    @Autowired
    STSReciever stsReciever;

    @Autowired
    SetTokenCookie setTokenCookie;


    public OAuth2RestOperations restTemplate;

    private OpenIdConnectUserDetails user;

    public OpenIdConnectFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(new NoopAuthenticationManager());
    }

    public void setRestTemplate(OAuth2RestTemplate restTemplate2) {
        restTemplate = restTemplate2;

    }


    //Obtains token from ID-Porten, parse it and verify signature/claims. Then sends request to STS and recieves a new token with extra information
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        //Tries to obtain access token from cookie storage, continues if failed. Should be updated to use local storage
        try {
            Cookie[] cookies = request.getCookies();
            int ok = 0;
            for (int i = 0; i < cookies.length; i++) {
                if (tokenValidation.verifyForCookie(cookies[i], sts_jwks)) {
                    ok++;
                }
            }
            if (ok > 0) {
                //TODO: Verify claims and expiration date
                //Right now only checks if the access token is there

                return user.get_upa_token();
            }
        }catch(Exception e){
            //Ignore and continue with login with ID-Porten
        }

        //Below are functions for logging in with ID-Porten and obtaining token
        OAuth2AccessToken accessToken;

        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            throw new BadCredentialsException("Could not obtain access token", e);
        }
        try {

            //Gets and parses token from ID-Porten
            String at = accessToken.toString();
            String kid = JwtHelper.headers(at).get("kid");
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(at, tokenValidation.verifier(kid, jwkUrl));
            Map<String, Object> authInfo = new ObjectMapper()
                    .readValue(tokenDecoded.getClaims(), Map.class);
            tokenValidation.verifyClaims(authInfo, issuer, clientId);

            //Sets cookie for ID-Porten
            setTokenCookie.setTokenCookie("idp_token", authInfo, accessToken.toString(), response);

            //Sends token to STS
            Map<String, Object> newAuthInfo = stsReciever.sendPostToSts(accessToken.toString(), stsReciever.getAuthorization(sts_username, sts_password), sts_url, sts_jwks);
            String newAccessToken = stsReciever.getNewToken();

            //Sets and saves user details and token
            OpenIdConnectUserDetails user = new OpenIdConnectUserDetails(newAuthInfo);
            this.user = user;
            UsernamePasswordAuthenticationToken upa_token =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            //Sets cookie for STS
            setTokenCookie.setTokenCookie("sts_token", newAuthInfo, newAccessToken, response);

            user.set_upa_token(upa_token);

            return upa_token;
        } catch (InvalidTokenException e) {
            throw new BadCredentialsException("Could not obtain user details from token", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class NoopAuthenticationManager implements AuthenticationManager {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
