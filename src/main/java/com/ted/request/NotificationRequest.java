package com.ted.request;

import com.ted.model.User;
import com.ted.model.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NotificationRequest {

    @NotBlank
    @Size(max = 30)
    private String action;

    private User from;
    private Post post;

    public NotificationRequest() {}

    public NotificationRequest(User from, String action, Post post) {
        this.from = from;
        this.action = action;
        this.post = post;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
