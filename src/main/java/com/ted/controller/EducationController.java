package com.ted.controller;

import com.ted.model.Education;
import com.ted.request.EducationRequest;
import com.ted.service.EducationService;
import com.ted.response.ApiResponse;
import com.ted.exception.NotAuthorizedException;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EducationController {

    @Autowired
    private EducationService educationService;

    // Adds an education for a user
    @PostMapping("/users/{userId}/education")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody EducationRequest educationRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Education education = educationService.create(userId, educationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{education}")
                .buildAndExpand(education.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Education Created.", education));
    }

    // Returns a user's education
    @GetMapping("/users/{userId}/education")
    @PreAuthorize("hasRole('USER')")
    public List<Education> getAll(@PathVariable(value = "userId") Long userId,
                                  @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return educationService.getAll(userId);
    }

}
