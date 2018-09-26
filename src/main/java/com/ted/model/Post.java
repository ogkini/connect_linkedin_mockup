package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import javax.persistence.*;
import java.util.*;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Posts", schema = "teddb")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @JsonIgnoreProperties("posts")
    @ManyToOne()
    @JoinColumn(name = "owner_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User owner;

    @Column(name = "text")
    private String text;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;

    // @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    // @Fetch(FetchMode.SELECT)
    // private List<Like> likes = new ArrayList<>();

    @Transient
    private int likesCount;

    public Post () {}

    public Post(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    // public List<Like> getLikes() {
    //     return likes;
    // }
    //
    // public void setLikes(List<Like> likes) {
    //     this.likes = likes;
    // }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post that = (Post) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, createdTime);
    }

}
