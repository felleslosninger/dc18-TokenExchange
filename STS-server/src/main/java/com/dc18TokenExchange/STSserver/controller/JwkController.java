package com.dc18TokenExchange.STSserver.controller;

import com.dc18TokenExchange.STSserver.CertificateDetails;
import com.dc18TokenExchange.STSserver.CertificateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;

@RestController
public class JwkController {

    private CertificateDetails certDetails = CertificateUtil.getCertificateDetails("C:\\temp\\keystore.jks","password");

    @GetMapping("/jwk")
    public String returnJwk(){
        X509Certificate cert = certDetails.getX509Certificate();

        try {
            System.out.println(cert.getExtendedKeyUsage());
            return cert.getCriticalExtensionOIDs().toString();
        } catch (CertificateParsingException e) {
            e.printStackTrace();
        }
        return "Fant ikke en drit ass";
    }
}
