package com.dc18TokenExchange.OIDCclient;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class OIDCTokenRetrieval {
    OIDCTokenRetrieval() {
    }

    @Value("${idp.clientId}")
    private String client_id;
    @Value("${idp.redirectUri}")
    private String redirect_uri;
    @Value("${idp.accessTokenUri}")
    private String POST_URL;

    void sendTokenReq(String codeNum) throws IOException {
        //Important parameters and variables
        String POST_PARAMS = "client_id=" + "oidc_dificamp_test" + "&grant_type=authorization_code&code=" + codeNum + "&redirect_uri=" + "http://localhost:8000/login";
        //String POST_PARAMS = "grant_type=authorization_code&code=" + codeNum + "&redirect_uri=" + "http://localhost:8000/login";
        String POST_URL = "https://oidc-test1.difi.eon.no/idporten-oidc-provider/token";
        String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";
        //Create parameters from HashMap
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("code", codeNum);
        parameters.put("client_id", "oidc_dificamp_test");
        parameters.put("redirect_uri", "localhost:8000/welcome");
        String params = ParameterStringBuilder.getParamsString(parameters);
        System.out.println(params);
        //For client authorization, client_id:client_secret
        String clientAuth = "oidc_dificamp_test:63a83c5c-617b-4f08-a9ec-244502185db7";
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());
        String clientAuthEncodedString = new String(clientAuthEncoded);
        //Checks variables (should be good)
        System.out.println("This is the parameters: " + POST_PARAMS);
        System.out.println("This is the token url: " + POST_URL);
        System.out.println("This is a ready made user agent (maybe unnecessary): " + USER_AGENT);
        System.out.println("This is the client_id:client_secret encoded with Base64: " + clientAuthEncodedString);
        //Sets up the POST request
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Http-method", "POST");
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Authorization", "Basic " + clientAuthEncodedString);
        //Sends in parameters into body (apparently)
        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(params);
        out.flush();
        out.close();
        // For POST only - END
        Map contentType = con.getHeaderFields();
        System.out.println("Request parameters" + contentType);
        //Gets response
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);
        //Tests response if it is 200 OK
        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
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