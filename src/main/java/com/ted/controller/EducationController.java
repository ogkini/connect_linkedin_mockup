package com.ted.controller;

import com.ted.exception.NotAuthorizedException;
import com.ted.model.Education;
import com.ted.request.EducationRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.EducationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class EducationController {

    private static final Logger logger = LoggerFactory.getLogger(EducationController.class);

    @Autowired
    private EducationService educationService;

    // Adds an education for a user
    @PostMapping("/users/{userId}/education")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody EducationRequest educationRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            logger.warn("The user tried to access had email: " + currentUser.getEmail());
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Education education = educationService.create(userId, educationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{educationId}")
                .buildAndExpand(education.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Education Created.", education));
    }

    // Returns a user's education
    @GetMapping("/users/{userId}/education")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Education> getAll(@PathVariable(value = "userId") Long userId,
                                  @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            // Don't show the private staff to other users..
            // Return null or empty Set ? --> Test how each case gets handled by the FrontEnd.
        }

        return educationService.getAll(userId);
    }

    // Deletes a specific user education
    @DeleteMapping("/users/{userId}/education/{educationId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "educationId") Long educationId,
                                        @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return educationService.deleteById(educationId, currentUser);
    }

}
