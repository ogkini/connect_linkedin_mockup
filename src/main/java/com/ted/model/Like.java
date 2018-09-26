package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Likes", schema = "teddb")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "post_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Post post;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User user;

    public Like () {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like that = (Like) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(post, that.post) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, post, user);
    }

}
