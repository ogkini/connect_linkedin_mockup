package com.ted.controller;

import com.ted.exception.NotAuthorizedException;
import com.ted.model.JobApply;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.JobApplyService;
import com.ted.service.RelationshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class JobApplyController {

    private static final Logger logger = LoggerFactory.getLogger(JobApplyController.class);

    @Autowired
    private JobApplyService jobApplyService;

    @Autowired
    private RelationshipService relationshipService;

    // A user applies to a jobOffer
    @PostMapping("/users/{userId}/jobOffers/{jobOfferId}/jobApplies")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "jobOfferId") Long jobOfferId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // A user can apply to a friend's jobOffer (not to his own jobOffer) -> not sure if this is the case now..
        if (currentUser.getId() != userId && !relationshipService.areConnected(userId, currentUser.getId())) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        JobApply jobApply = jobApplyService.create(userId, jobOfferId, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{jobApplyId}")
                .buildAndExpand(jobApply.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "JobApply Created.", jobApply));
    }

    // A user deletes a jobApply
    @DeleteMapping("/users/{userId}/jobOffers/{jobOfferId}/jobApplies/{jobApplyId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteById(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "jobOfferId") Long jobOfferId,
                                        @PathVariable(value = "jobApplyId") Long jobApplyId,
                                        @Valid @CurrentUser UserDetailsImpl currentUser) {

        logger.debug("Going to delete jobApply: " + jobApplyId);

        return jobApplyService.deleteById(jobApplyId, jobOfferId, userId, currentUser);
    }

}
