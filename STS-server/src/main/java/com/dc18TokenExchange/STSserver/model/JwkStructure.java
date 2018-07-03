/*package com.dc18TokenExchange.STSserver.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "jwk_configs")
public class JwkStructure {

    //Jwk ID
    @Id
    @GeneratedValue(generator = "jwk_generator")
    @SequenceGenerator(
            name = "jwk_generator",
            sequenceName = "jwk_sequence",
            initialValue = 1000
    )
    private Long id;

    //Encryption method
    @NotBlank
    private String kty;

    //Key exponent
    @NotBlank
    private String e;

    //Use type
    @NotBlank
    private String use;

    //Kid
    @NotBlank
    private String kid;

    //x5c
    @NotBlank
    @ElementCollection
    private List<String> x5c;

    //Modulus
    @NotBlank
    private String n;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public List<String> getX5c() {
        return x5c;
    }

    public void setX5c(List<String> x5c) {
        this.x5c = x5c;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }
}*/
