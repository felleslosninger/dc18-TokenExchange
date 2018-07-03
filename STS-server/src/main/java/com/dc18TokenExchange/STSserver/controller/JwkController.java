package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.CertificateDetails;
import com.dc18TokenExchange.STSserver.CertificateUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

@RestController
public class JwkController {

    @Value("${jwks.kty}")
    private String kty;

    @Value("${jwks.e}")
    private String e;

    @Value("${jwks.use}")
    private String use;

    @Value("${jwks.kid}")
    private String kid;

    @Value("${jwks.n}")
    private String n;

    @Value("${jwks.x5c}")
    private String x5c;

    @Value("${jwks.alg}")
    private String alg;

    @Value("${jwks.x5t}")
    private String x5t;


    private CertificateDetails certDetails = CertificateUtil.getCertificateDetails("C:\\temp\\keystore.jks","password");

    @GetMapping("/jwk")
    public String returnJwk(){
        X509Certificate cert = certDetails.getX509Certificate();

        List<JSONObject> keysList = new ArrayList<>();
        List<String> x5cList = new ArrayList<>();

        x5cList.add(x5c);

        keysList.add(new JSONObject()
                .put("kty",kty)
                .put("e",e)
                .put("use",use)
                .put("kid",kid)
                .put("n",n)
                .put("x5c", x5cList)
                .put("alg",alg)
                .put("x5t",x5t));

        return new JSONObject().put("keys", keysList).toString();

        /*System.out.println("Public key\n"+cert.getPublicKey());
        System.out.println("Cert\n"+cert.getCriticalExtensionOIDs().toString());

        String n = "18905438646391490756727835967114228353730478196999644751024835704888498591924177296167376951656041010201912596154875498147011318569045398781417458675387813628578788096244310986836031711017140666814626920975515217223628656197280722048747509745680939203845426543222871182264973890190391224950136781815319002287324964034240718045003457055082599595547854458487848244807201148154766777824501105569742181560011241178896055174204843648146528667799458221705636003935184276960580812337414147180141943864258456703003573935427986318226178412620520492154244022825168737505440780137880624524720623530173109032166400435587401379847";
        String e = "65537";
        Base64 base64 = new Base64(true);
        n = new String(base64.encodeBase64(n.getBytes()));
        e = new String(base64.encodeBase64(e.getBytes()));
        System.out.println(n +"\n" + e);

        return "Hey";*/
    }
}
