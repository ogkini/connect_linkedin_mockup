package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Conversations", schema = "teddb")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_one_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User userOne;

    @ManyToOne()
    @JoinColumn(name = "user_two_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User userTwo;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Message> messages = new ArrayList<>();

    public Conversation () {}

    public Conversation(User userOne, User userTwo) {
        this.userOne = userOne;
        this.userTwo = userTwo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserOne() {
        return userOne;
    }

    public void setUserOne(User userOne) {
        this.userOne = userOne;
    }

    public User getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(User userTwo) {
        this.userTwo = userTwo;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userOne, that.userOne) &&
                Objects.equals(userTwo, that.userTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userOne, userTwo);
    }

}
