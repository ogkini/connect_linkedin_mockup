package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Users", schema = "teddb", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
           "email"
    })
})
@JsonPropertyOrder(
    {"id", "firstname", "lastname", "email", "picture", "occupation", "experiences", "educations", "skills",
            "relationships", "posts", "likes"}
)
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @Id
    @XmlAttribute(name = "_id")
    @XmlID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @XmlElement
    @Column(name = "firstname")
    private String firstname;

    @XmlElement
    @Column(name = "lastname")
    private String lastname;

    @XmlElement
    @Column(name = "email")
    private String email;

    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @XmlElement
    @Column(name = "picture")
    private String picture;

    @XmlElement
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private Occupation occupation;

    @XmlElement
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Experience> experiences = new ArrayList<>();

    @XmlElement
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Education> educations = new ArrayList<>();

    @XmlElement
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Skill> skills = new ArrayList<>();

    //@XmlElement
    @JsonIgnoreProperties("sender")
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Relationship> relationships = new ArrayList<>();

    //@XmlElement
    @JsonIgnoreProperties("owner")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Post> posts = new ArrayList<>();

    //@XmlElement
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Like> likes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private int newFriendRequests;

    @Transient
    private boolean relationshipBetween;

    public User () {}

    public User(String firstname, String lastname, String email, String password, String picture) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.picture = picture;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    public List<Experience> getExperience() {
        return experiences;
    }

    public void setExperience(List<Experience> expreriences) {
        this.experiences = expreriences;
    }

    public List<Education> getEducation() {
        return educations;
    }

    public void setEducation(List<Education> educations) {
        this.educations = educations;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getNewFriendRequests() {
        return newFriendRequests;
    }

    public void setNewFriendRequests(int newFriendRequests) {
        this.newFriendRequests = newFriendRequests;
    }

    public boolean getRelationshipBetween() {
        return relationshipBetween;
    }

    public void setRelationshipBetween(boolean relationshipBetween) {
        this.relationshipBetween = relationshipBetween;
    }

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
