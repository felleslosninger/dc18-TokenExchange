package com.dc18TokenExchange.Resourceserver.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "workplace_info_tableDAO")
public class WorkplaceDAO{

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

    //Absolute path for image file, used to for POST-mapping entries to database. Should be improved.
    private String logo_path;

    //Absolute path for image file, used to for POST-mapping entries to database. Should be improved.
    private String background_path;

    private String home_url;


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

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public String getBackground_path() {
        return background_path;
    }

    public void setBackground_path(String background_path) {
        this.background_path = background_path;
    }

    public String getHome_url() {
        return home_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }
}
