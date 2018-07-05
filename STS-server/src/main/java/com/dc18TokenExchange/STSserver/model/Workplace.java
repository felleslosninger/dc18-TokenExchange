package com.dc18TokenExchange.STSserver.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
@Table(name = "workplace_info_table")
public class Workplace {

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

    //Organization number for organization
    @NotBlank
    @Size(min = 3, max = 30)
    private String orgName;


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
}
