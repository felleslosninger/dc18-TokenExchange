package com.dc18TokenExchange.STSserver;


import com.dc18TokenExchange.STSserver.service.WorkplaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class TokenGenerator {

    @Value("${jwks.kid}")
    private String kid;

    @Value("${sts.iss}")
    private String iss;

    @Value("${cert.filepath}")
    private String filepath;

    @Value("${cert.password}")
    private String password;

    @Autowired
    WorkplaceService workplaceService;


    //Gets claims from received token
    public Map<String,Object> getTokenParts(String token, int part){
        String[] split_string = token.split("\\.");
        String base64EncodedPart = split_string[part];
        Base64 base64Url = new Base64(true);
        String stringPart = new String(base64Url.decode(base64EncodedPart));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> partMap = null;

        try {
            partMap = mapper.readValue(stringPart, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return partMap;
    }

    //Inserts new claims into new token
    public String getNewToken(Map<String, Object> header, Map<String, Object> body, String workplaceName, Long workplaceNum, String firstName, String lastName){
        ObjectMapper mapper = new ObjectMapper();
        Base64 base64Url = new Base64(true);

        header.replace("kid", kid);
        body.replace("iss", iss);
        body.put("wrk_name", workplaceName);
        body.put("wrk_num", workplaceNum);
        body.put("f_name", firstName);
        body.put("l_name", lastName);
        String headerNew = null;
        String bodyNew = null;

        try {
            headerNew = mapper.writeValueAsString(header);
            bodyNew = mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String headerEncoded = new String(base64Url.encode(headerNew.getBytes()));
        String bodyEncoded = new String(base64Url.encode(bodyNew.getBytes()));


        String tokenNew = headerEncoded + "." + bodyEncoded + "." + getNewSignature(headerEncoded, bodyEncoded);
        tokenNew = tokenNew.replaceAll("\\r|\\n", "");
        return tokenNew;
    }

    //Gets new token signature
    public String getNewSignature(String headerEncoded, String bodyEncoded){
        String toBeEncoded = headerEncoded + "." + bodyEncoded;
        toBeEncoded = toBeEncoded.replaceAll("\\r|\\n", "");

        try {
            return sign(toBeEncoded);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Signs new token
    public String sign(String encodeText) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        //Gets STS-private key information
        CertificateDetails certDetails = CertificateUtil.getCertificateDetails(filepath,password);

        PrivateKey pk = certDetails.getPrivateKey();

        Base64 base64 = new Base64(true);

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(pk);
        privateSignature.update(encodeText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return new String(base64.encode(signature));
    }

}
