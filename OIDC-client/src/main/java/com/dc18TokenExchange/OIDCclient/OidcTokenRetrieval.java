package com.dc18TokenExchange.OIDCclient;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class OidcTokenRetrieval{

    private String code;


    public OidcTokenRetrieval(String code){
        this.code = code;
    }


    //@Value("${idp.clientId}")
    //private String client_id;

    //@Value("${idp.redirectUri}")
    //private String redirect_uri;

    //@Value("${idp.accessTokenUri}")
    //private String POST_URL;


    public void sendTokenReq() throws IOException {

        //Important parameters and variables
        String POST_PARAMS = "client_id=" + "oidc_dificamp_test" + "&grant_type=authorization_code&code=" + code + "&redirect_uri=" + "http://localhost:8000/login";
        //String POST_PARAMS = "grant_type=authorization_code&code=" + codeNum + "&redirect_uri=" + "http://localhost:8000/login";
        //String POST_PARAMS = "code=" + code;
        String POST_URL = "https://oidc-test1.difi.eon.no/idporten-oidc-provider/jwk";
        String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";

        //For client authorization, client_id:client_secret
        String clientAuth = "oidc_dificamp_test:63a83c5c-617b-4f08-a9ec-244502185db7";
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());
        String clientAuthEncodedString = new String(clientAuthEncoded);

        //Checks variables (should be good)
        System.out.println("Dette er parametere: "+POST_PARAMS);
        System.out.println("Dette er token url: "+POST_URL);
        System.out.println("Dette er ferdiglaget user agent (kanskje unødvendig): "+USER_AGENT);
        System.out.println("Dette er client_id:client_secret encoded med Base64: "+clientAuthEncodedString);

        //Sets up the POST request
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Http-metode", "POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Authorization", "Basic " + clientAuthEncodedString);

        //Sends in parameters into body (appearantly)
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        //Gets response
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        //Tests response if it is 200 OK
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request error");
        }
    }
}
