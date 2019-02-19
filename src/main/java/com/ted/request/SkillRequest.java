package com.ted.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SkillRequest {

    @NotBlank
    @Size(max = 45)
    private String name;

    // This may be blank.
    @Size(max = 45)
    private String strength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

}
