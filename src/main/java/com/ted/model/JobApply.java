package com.ted.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "JobApplies", schema = "teddb")
public class JobApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_apply_id")
    private Long id;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "job_offer_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private JobOffer jobOffer;

    @JsonIgnoreProperties({"comments", "posts", "jobApplies", "jobOffers"})
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User user;

    public JobApply () {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOffer jobOffer) { this.jobOffer = jobOffer; }

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
        JobApply that = (JobApply) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(jobOffer, that.jobOffer) &&
                Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobOffer, user);
    }


}
