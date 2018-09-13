package com.ted.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Date;

public class EducationRequest {

    @NotBlank
    @Size(max = 80)
    private String title;

    @NotBlank
    @Size(max = 45)
    private String school;

    private Date startDate;
    private Date endDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
