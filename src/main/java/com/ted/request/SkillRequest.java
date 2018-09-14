package com.ted.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SkillRequest {

    @NotBlank
    @Size(max = 45)
    private String name;

    @NotBlank
    private int strength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

}
