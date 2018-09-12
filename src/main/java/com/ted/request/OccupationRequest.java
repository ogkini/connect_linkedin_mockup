package com.ted.request;

import javax.validation.constraints.*;

public class OccupationRequest {

    @NotBlank
    @Size(max = 80)
    private String title;

    @NotBlank
    @Size(max = 45)
    private String company;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

}
