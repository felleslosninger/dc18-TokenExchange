package com.dc18TokenExchange.STSserver.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "UserInfoDTO")
public class UserInfoDTO {
    //ID for user
    @Id
    private long userId;

    //User first name(s)
    @NotBlank
    @Size(min = 3, max = 30)
    private String firstName;

    //User last name
    @NotBlank
    @Size(min = 3, max = 30)
    private String lastName;

    private Long orgNum;

    
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getOrgNum() {
        return orgNum;
    }

    public void setOrgNum(Long orgNum) {
        this.orgNum = orgNum;
    }
}
