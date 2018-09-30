package com.ted.controller;

import com.ted.exception.NotAuthorizedException;
import com.ted.model.JobOffer;
import com.ted.request.JobOfferRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class JobOfferController {

    @Autowired
    private JobOfferService jobOfferService;

    // A user creates a jobOffer
    @PostMapping("/users/{userId}/jobOffers")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody JobOfferRequest jobOfferRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {

        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        JobOffer jobOffer = jobOfferService.create(userId, jobOfferRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{jobOfferId}")
                .buildAndExpand(jobOffer.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "JobOffer Created.", jobOffer));
    }

    // Returns a user's jobOffers
    @GetMapping("/users/{userId}/jobOffers")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<JobOffer> getAll(@PathVariable(value = "userId") Long userId,
                             @Valid @CurrentUser UserDetailsImpl currentUser) {
        return jobOfferService.getAll(userId, currentUser);
    }

}
