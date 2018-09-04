package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Users", schema = "teddb", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
           "email"
    })
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "email")
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    // @JsonIgnore
    // @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinTable(name = "UserRoles",
    //         joinColumns = @JoinColumn(name = "user_id", referencedColumnName="id"),
    //         inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName="id"))
    // private Set<Role> roles = new HashSet<>();

    // @JsonIgnore
    // @ManyToMany(mappedBy = "members", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    // private Set<Team> teams = new HashSet<>();

    // @JsonIgnore
    // @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    // private Set<Notification> notifications;

    public User () {}

    public User(String firstname, String lastname, String password, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // public Set<Role> getRoles() {
    //     return roles;
    // }

    // public void setRoles(Set<Role> roles) {
    //     this.roles = roles;
    // }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(firstname, that.firstname) &&
                Objects.equals(lastname, that.lastname) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, password, email);
    }

}