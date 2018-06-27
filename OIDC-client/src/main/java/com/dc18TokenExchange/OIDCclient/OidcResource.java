package com.dc18TokenExchange.OIDCclient;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OidcResource {

// mulig disse må være lik det i properties? dont know

    @JsonProperty(value = "issuer")
    private String issuer;

    @JsonProperty(value = "athorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty(value = "token_endpoint")
    private String tokenEndpoint;

    @JsonProperty(value = "jwks_uri")
    private String jwkEndpoint;


    public String getIssuer(){
        return issuer;
    }

    public String getJwkEndpoint(){
        return jwkEndpoint;
    }


}
