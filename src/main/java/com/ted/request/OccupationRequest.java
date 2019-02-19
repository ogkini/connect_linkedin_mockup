package com.ted.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class OccupationRequest {

    @NotBlank
    @Size(max = 80)
    private String title;

    // This may be blank.
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
