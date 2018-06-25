package com.dc18TokenExchange.OIDCclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class OidcPostRequest {

    private final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

    String tokenURL = "https://oidc-test1.difi.eon.no/idporten-oidc-provider/token";

    String code;

    String clientId = "oidc_dificamp_test";

    String clientSecret = "63a83c5c-617b-4f08-a9ec-244502185db7";

    String redirectUri = "http://localhost:8000/welcome";

    public OidcPostRequest(String code){
        this.code = code;
    }

    public String getAuthorization(){
        String clientAuth = clientId+":"+clientSecret;
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());
        return new String(clientAuthEncoded);
    }

    public void sendPost() throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(tokenURL);

        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Authorization", "Basic "+getAuthorization() );
        post.setHeader("Content-type", "application/x-www-form-urlencoded");

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("code",code));
        urlParameters.add(new BasicNameValuePair("grant_type","authorization_code"));
        urlParameters.add(new BasicNameValuePair("client_id",clientId));
        urlParameters.add(new BasicNameValuePair("redirect_uri",redirectUri));


        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + tokenURL);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());
    }
}
