package com.ted.response;

public class SignInResponse {

    private String accessToken;
    private String tokenType = "Bearer";

    private String email;
    private String firstname;
    private String lastname;

    public SignInResponse(String accessToken, String email, String firstname, String lastname) {
        this.accessToken = accessToken;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

}
