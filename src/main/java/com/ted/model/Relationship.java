package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Relationships", schema = "teddb")
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_one_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "user_two_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User receiver;

    @Column(name = "status")
    private int status;

    @Column(name = "seen")
    private boolean seen;

    public Relationship () {}

    public Relationship(User sender, User receiver, int status, boolean seen) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.seen = seen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relationship that = (Relationship) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(receiver, that.receiver) &&
                status == that.status &&
                seen == that.seen;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, status, seen);
    }

}
