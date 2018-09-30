package com.ted.repository;

import com.ted.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobOfferRepository  extends JpaRepository<JobOffer, Long> {

    // Returns a user's jobOffer
    @Query("select jo from JobOffer jo where jo.owner.id = :userId")
    List<JobOffer> getAllByUserId(@Param("userId") Long userId);

    // Returns a specific jobOffer of a specific user
    @Query("select jo from JobOffer jo where jo.id = :jobOfferId and jo.owner.id = :userId")
    Optional<JobOffer> findByIdAndUserId(@Param("jobOfferId") Long jobOfferId, @Param("userId") Long userId);

}
