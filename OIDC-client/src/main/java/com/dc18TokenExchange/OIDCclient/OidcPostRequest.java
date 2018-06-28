package com.dc18TokenExchange.OIDCclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class OidcPostRequest {

    private final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";
    private String tokenURL = "https://oidc-test1.difi.eon.no/idporten-oidc-provider/token";
    private String code;
    private String clientId = "oidc_dificamp_test";
    private String clientSecret = "673f897e-873a-443a-9aa0-e92541534726";
    private String redirectUri = "http://localhost:8000/welcome";

    public OidcPostRequest(String code){
        this.code = code;
    }

    public String getAuthorization(){
        String clientAuth = clientId+":"+clientSecret;
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());

        return new String(clientAuthEncoded);
    }

    private String accessToken;

    public void sendPost() throws Exception{

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(tokenURL);

        post.setHeader(new BasicHeader("User-Agent", USER_AGENT));
        post.setHeader(new BasicHeader("Authorization", "Basic "+getAuthorization()));
        post.setHeader(new BasicHeader("Content-type", "application/x-www-form-urlencoded"));

        Header[] headers = post.getAllHeaders();
        for (int i = 0; i < headers.length; i++){
            System.out.println("header: " + headers[i].toString());
        }

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("code",code));
        urlParameters.add(new BasicNameValuePair("grant_type","authorization_code"));
        urlParameters.add(new BasicNameValuePair("client_id", clientId));
        urlParameters.add(new BasicNameValuePair("redirect_uri", redirectUri));
        System.out.println("dette er urlParameterene; " +urlParameters);


        post.setEntity(new UrlEncodedFormEntity(urlParameters));



        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + tokenURL);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());


        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        Map<String,String> map;

        String json = rd.readLine();

        map = new ObjectMapper().readValue(json, HashMap.class);
        client.close();

        System.out.println("AccessToken: " + map.get("access_token"));
        this.accessToken = map.get("access_token");
    }

    public String getAccessToken(){
        return accessToken;
    }

}
