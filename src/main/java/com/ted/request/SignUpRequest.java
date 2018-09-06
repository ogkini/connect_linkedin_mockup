package com.ted.request;

import javax.validation.constraints.*;

import java.util.Date;

/*
 * This class provides the json specification
 * for a Sign Up request.
 */
public class SignUpRequest {

    @NotBlank
    @Size(min = 2, max = 45)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 45)
    private String lastname;

    @NotBlank
    private Date birthdate;

    @NotBlank
    @Size(max = 65)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 100)
    private String password;


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

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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

}
