package com.ted.service;

import com.ted.exception.NotAuthorizedException;
import com.ted.exception.ResourceNotFoundException;
import com.ted.model.JobApply;
import com.ted.model.JobOffer;
import com.ted.model.User;
import com.ted.repository.JobApplyRepository;
import com.ted.repository.JobOfferRepository;
import com.ted.repository.UserRepository;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JobApplyService {

    @Autowired
    private JobApplyRepository jobApplyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(JobApplyService.class);

    // A user jobApplys a jobOffer
    public JobApply create(Long userId, Long jobOfferId, UserDetailsImpl currentUser) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        JobOffer jobOffer = jobOfferRepository.findByIdAndUserId(jobOfferId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("JobOffer", "id", jobOfferId));

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        JobApply jobApply = new JobApply();

        jobApply.setJobOffer(jobOffer);
        jobApply.setUser(user);

        return jobApplyRepository.save(jobApply);
    }

    // Returns the number of jobApplies of a jobOffer
    public int getJobAppliesCount(Long jobOfferId) {
        return jobApplyRepository.getAllByJobOfferId(jobOfferId).size();
    }

    // A user deletes a jobApply
    public ResponseEntity<?> deleteById(Long jobApplyId, Long jobOfferId, Long userId, UserDetailsImpl currentUser) {
        JobApply jobApply = validatePathService.validatePathAndGetJobApply(jobApplyId, jobOfferId, userId);

        // Check if the jobApply belongs to the current user
        if (currentUser.getId() != jobApply.getUser().getId()) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        jobApplyRepository.delete(jobApply);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted jobApply."));
    }

}
