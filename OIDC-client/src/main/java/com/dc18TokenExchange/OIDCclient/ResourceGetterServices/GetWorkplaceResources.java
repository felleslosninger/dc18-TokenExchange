package com.dc18TokenExchange.OIDCclient.ResourceGetterServices;

import org.apache.http.HttpEntity;
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
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetWorkplaceResources {

    @Value("${resource.server.username}")
    private String username;

    @Value("${resource.server.password}")
    private String password;

    @Value("${resource.server.url}")
    private String url;


    //Sends request to resource server in order to
    public void getWorkplaceLogo(int orgNum) throws IOException {
        String auth = makeAuthorization(username, password);

        String orgNumString = String.valueOf(orgNum);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url + "/workplace/logo");

        post.setHeader(new BasicHeader("Authorization", "Basic "+auth));
        post.setHeader(new BasicHeader("Content-type", "application/x-www-form-urlencoded"));

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("orgNum", orgNumString));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();
        //BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        entity.writeTo(baos);

        byte[] logoBytes = baos.toByteArray();

        client.close();

        System.out.println(logoBytes.toString());

        saveImage("C:\\temp\\newImage.png", logoBytes);
        //return logoBytes;
    }

    //Creates authorizaion header for resource server authorization
    public String makeAuthorization(String resource_server_username, String resource_server_password){
        String clientAuth = resource_server_username+":"+resource_server_password;
        byte[] clientAuthEncoded = Base64.encodeBase64(clientAuth.getBytes());
        return new String(clientAuthEncoded);
    }

    public void saveImage(String newPath, byte[] logoBytes){

        try{
            FileOutputStream fos = new FileOutputStream(newPath);
            fos.write(logoBytes);
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

