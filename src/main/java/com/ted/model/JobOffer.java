package com.ted.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "JobOffers", schema = "teddb")
public class JobOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_offer_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "owner_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User owner;

    @Column(name = "text")
    private String text;

    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private Timestamp createdTime;

    @OneToMany(mappedBy = "jobOffer", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<JobApply> jobApplies = new ArrayList<>();

    // Indicates whether the user requesting the job_offer
    // has applied to it or not (for button display purposes)
    @Transient
    private boolean appliedToJobOffer;

    @Transient
    private int jobAppliesCount;

    public JobOffer () {}

    public JobOffer(String text) {
        this.text = text;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public List<JobApply> getJobApplies() {
        return this.jobApplies;
    }

    public void setJobApplies(List<JobApply> jobApplies) {
        this.jobApplies = jobApplies;
    }

    public boolean getAppliedToJobOffer() {
        return this.appliedToJobOffer;
    }

    public void setAppliedToJobOffer(boolean appliedToJobOffer) {
        this.appliedToJobOffer = appliedToJobOffer;
    }

    public int getJobAppliesCount() {
        return this.jobAppliesCount;
    }

    public void setJobAppliesCount(int jobAppliesCount) {
        this.jobAppliesCount = jobAppliesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobOffer that = (JobOffer) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(text, that.text) &&
                Objects.equals(createdTime, that.createdTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, createdTime);
    }

}
