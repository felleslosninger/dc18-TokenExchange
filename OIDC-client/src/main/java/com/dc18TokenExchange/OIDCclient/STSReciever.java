package com.dc18TokenExchange.OIDCclient;

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
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class STSReciever {

    @Autowired
    TokenValidation tokenValidation;

    private String newToken;

    //Sends POST to STS, requests new token
    public Map<String, Object> sendPostToSts(String accessToken, String auth, String sts_url, String sts_jwks) throws IOException {
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
        this.newToken = newToken;

        client.close();

        String kid = JwtHelper.headers(newToken).get("kid");

        try {
            Jwt tokenDecoded = JwtHelper.decodeAndVerify(newToken, tokenValidation.verifier(kid, sts_jwks));
            return new ObjectMapper()
                    .readValue(tokenDecoded.getClaims(), Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Authorizes for STS connection
    public String getAuthorization(String sts_username, String sts_password){
        String clientAuth = sts_username+":"+sts_password;
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());
        return new String(clientAuthEncoded);
    }

    public String getNewToken(){
        return this.newToken;
    }
}
