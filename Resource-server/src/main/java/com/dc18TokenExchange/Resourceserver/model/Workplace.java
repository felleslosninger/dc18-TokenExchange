package com.dc18TokenExchange.Resourceserver.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "workplace_info_table")
public class Workplace /*extends AuditModel */{

    //Generates ID for every organization
    /*@Id
    @GeneratedValue(generator = "workplace_generator")
    @SequenceGenerator(
            name = "workplace_generator",
            sequenceName = "workplace_sequence",
            initialValue = 100000000
    )
    private Long id;*/

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
    private byte[] image;


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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
