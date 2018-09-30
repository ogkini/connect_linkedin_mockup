package com.ted.request;

import com.ted.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String picture;


    public User asUser() {

        return new User(
                this.getId(),
                this.getFirstname(),
                this.getLastname(),
                this.getEmail(),
                this.getPassword(),
                this.getPicture()  // It may be null since it's optional, no problem.
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}
