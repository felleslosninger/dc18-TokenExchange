package com.dc18TokenExchange.Resourceserver.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "workplace_info_table")
public class Workplace /*extends AuditModel */{ //Auditmodel won't work for some reason, server claims all params are null when creating entity

    //Organization number for organization (Brønnøysund)
    @Id
    private long orgNum;

    //Organization name for organization
    @NotBlank
    @Size(min = 3, max = 30)
    private String orgName;

    //HEX for primary color
    @NotBlank
    private String pri_col;

    //HEX for secondary color
    @NotBlank
    private String sec_col;

    //Bytes for image logo
    //@Lob
    @Column(name="WORKPLACE_IMAGE", nullable=true, columnDefinition="bytea")
    private byte[] logo_img;

    //Bytes for image background
    @Column(name="WORKPLACE_BACKGROUND_IMAGE", nullable=true, columnDefinition="bytea")
    private byte[] background_img;

    private String home_url;


    //Getters and setters
    public long getOrgNum() {
        return orgNum;
    }

    public void setOrgNum(long orgNum) {
        this.orgNum = orgNum;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getPri_col() {
        return pri_col;
    }

    public void setPri_col(String pri_col) {
        this.pri_col = pri_col;
    }

    public String getSec_col() {
        return sec_col;
    }

    public void setSec_col(String sec_col) {
        this.sec_col = sec_col;
    }

    public byte[] getLogo_img() {
        return logo_img;
    }

    public void setLogo_img(byte[] logo_img) {
        this.logo_img = logo_img;
    }

    public byte[] getBackground_img() {
        return background_img;
    }

    public void setBackground_img(byte[] background_img) {
        this.background_img = background_img;
    }

    public String getHome_url() {
        return home_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }
}
