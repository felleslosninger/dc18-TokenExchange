package com.dc18TokenExchange.STSserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_info_table")
public class UserInfo extends AuditModel {

    //Generates ID for every user
    /*@Id
    @GeneratedValue(generator = "user_generator")
    @SequenceGenerator(
            name = "user_generator",
            sequenceName = "user_sequence",
            initialValue = 1000
    )
    private Long id;*/

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

    //Sets user affiliation to established organization (can be NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orgNum", nullable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @JsonIgnore
    private Workplace worksFor;


    //Getters and setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public Workplace getWorksFor() {
        return worksFor;
    }

    public void setWorksFor(Workplace worksFor) {
        this.worksFor = worksFor;
    }
}
