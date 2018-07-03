package com.dc18TokenExchange.OIDCclient;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.codec.binary.Base64;
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


    public OAuth2RestOperations restTemplate;

    public OpenIdConnectFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(new NoopAuthenticationManager());
    }

    public void setRestTemplate(OAuth2RestTemplate restTemplate2) {
        restTemplate = restTemplate2;

    }


    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            throw new BadCredentialsException("Could not obtain access token", e);
        }
        try {
            String at = accessToken.toString();
            System.out.println("AccessToken: " + at);
            String kid = JwtHelper.headers(at).get("kid");
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(at, verifier(kid, jwkUrl));
            Map<String, Object> authInfo = new ObjectMapper()
                    .readValue(tokenDecoded.getClaims(), Map.class);
            System.out.println(authInfo.keySet());
            System.out.println(authInfo.get("aud"));
            System.out.println(authInfo.get("iss"));
            System.out.println(authInfo.get("exp").toString());
            verifyClaims(authInfo);

            //localhost:8080/getNewToken
            //Mottar token fra STS
            //Bruker accesstoken til user under

            sendPostToSts(accessToken.toString(), getAuthorization());

            OpenIdConnectUserDetails user = new OpenIdConnectUserDetails(authInfo, accessToken);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
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

    private RsaVerifier verifier(String kid, String url) throws Exception {
        JwkProvider provider = new UrlJwkProvider(new URL(url));
        Jwk jwk = provider.get(kid);
        return new RsaVerifier((RSAPublicKey) jwk.getPublicKey());
    }

    public void verifyClaims(Map claims) {
        int exp = (int) claims.get("exp");
        Date expireDate = new Date(exp * 1000L);
        Date now = new Date();
        if (!claims.get("iss").equals(issuer) ||
                !claims.get("aud").equals(clientId)) {
           throw new RuntimeException("Invalid claims");
        }

    }

    //Authorizes for STS connection
    public String getAuthorization(){
        String clientAuth = sts_username+":"+sts_password;
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());
        return new String(clientAuthEncoded);
    }

    //Sends POST to STS, requests new token
    public void sendPostToSts(String accessToken, String auth) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(sts_url);

        post.setHeader(new BasicHeader("Authorization", "Basic "+auth));
        post.setHeader(new BasicHeader("Content-type", "application/x-www-form-urlencoded"));

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("access_token",accessToken));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        Map<String,String> map;
        String newToken = rd.readLine();
        //map = new ObjectMapper().readValue(json, HashMap.class);
        client.close();

        System.out.println(newToken);

        String kid = JwtHelper.headers(newToken).get("kid");
        System.out.println("Kid is: "+kid);

        try {
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(newToken, verifier(kid, sts_jwks));
            Map<String, Object> newAuthInfo = new ObjectMapper()
                    .readValue(tokenDecoded.getClaims(), Map.class);
            System.out.println(newAuthInfo.keySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}