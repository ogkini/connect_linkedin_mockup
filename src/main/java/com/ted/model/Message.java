package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Messages", schema = "teddb")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "conversation_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Conversation conversation;

    @ManyToOne()
    @JoinColumn(name = "sender_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User sender;

    @ManyToOne()
    @JoinColumn(name = "receiver_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User receiver;

    @Column(name = "text")
    private String text;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;

    @Column(name = "seen")
    private boolean seen;

    @Column(name = "opened")
    private boolean opened;

    public Message () {}

    public Message(User sender, User receiver, String text, boolean seen, boolean opened) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.seen = seen;
        this.opened = opened;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
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

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(receiver, that.receiver) &&
                Objects.equals(text, that.text) &&
                seen == that.seen &&
                opened == that.opened;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver, text, seen, opened);
    }

}
