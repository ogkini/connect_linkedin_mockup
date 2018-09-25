package com.ted.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Date;

public class PostRequest {

    @NotBlank
    @Size(max = 500)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
