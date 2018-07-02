package com.dc18TokenExchange.STSserver;

import com.dc18TokenExchange.STSserver.service.WorkplaceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.security.*;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
public class TokenGenerator {

    @Autowired
    WorkplaceService workplaceService;

    //final Charset asciiCs = Charset.forName("US-ASCII");
    //final Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

    //CertificateDetails certDetails = CertificateUtil.getCertificateDetails("C:\\Users\\camp-pry\\X509_certificate.cer","password");
    private CertificateDetails certDetails = CertificateUtil.getCertificateDetails("C:\\temp\\keystore.jks","password");

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

    public String getWork(Map<String, Object> map, String pid){
        String userPid = map.get(pid).toString();
        Long userPidLong = Long.parseLong(userPid);
        System.out.println(userPidLong);
        String userWorkplace = workplaceService.getDistinctWorkplaceByUserIdAsString(userPidLong);
        System.out.println(userWorkplace);
        return userWorkplace;
    }

    public String getNewToken(Map<String, Object> header, Map<String, Object> body, String newClaim, Object newClaimValue){
        ObjectMapper mapper = new ObjectMapper();
        Base64 base64Url = new Base64(true);

        body.put(newClaim, newClaimValue);
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

    public String sign(String encodeText) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        PrivateKey pk = certDetails.getPrivateKey();
        System.out.println(certDetails.getX509Certificate());

        Base64 base64 = new Base64(true);

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(pk);
        privateSignature.update(encodeText.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return new String(base64.encode(signature));
    }

}
