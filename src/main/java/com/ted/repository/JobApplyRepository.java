package com.ted.repository;

import com.ted.model.JobApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplyRepository  extends JpaRepository<JobApply, Long> {

    // Returns a jobOffer's jobApplies
    @Query("select ja from JobApply ja where ja.jobOffer.id = :jobOfferId")
    List<JobApply> getAllByJobOfferId(@Param("jobOfferId") Long jobOfferId);

    // Returns a specific jobApply of specific jobOffer of a specific user
    @Query("select ja from JobApply ja where ja.id = :jobApplyId and ja.jobOffer.id = :jobOfferId and ja.jobOffer.owner.id = :userId")
    Optional<JobApply> findByIdAndJobOfferIdAndUserId(@Param("jobApplyId") Long jobApplyId,
                                                      @Param("jobOfferId") Long jobOfferId, @Param("userId") Long userId);
}
