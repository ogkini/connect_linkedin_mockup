package com.ted.controller;

import com.ted.exception.NotAuthorizedException;
import com.ted.model.Experience;
import com.ted.request.ExperienceRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.ExperienceService;

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
public class ExperienceController {

    private static final Logger logger = LoggerFactory.getLogger(ExperienceController.class);

    @Autowired
    private ExperienceService experienceService;

    // Adds an experience for a user
    @PostMapping("/users/{userId}/experience")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody ExperienceRequest experienceRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Experience experience = experienceService.create(userId, experienceRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{experienceId}")
                .buildAndExpand(experience.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Experience Created.", experience));
    }

    // Returns a user's experience
    @GetMapping("/users/{userId}/experience")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Experience> getAll(@PathVariable(value = "userId") Long userId,
                                   @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            // Don't show the private staff to other users..
            // Return null or empty Set ? --> Test how each case gets handled by the FrontEnd.
        }

        return experienceService.getAll(userId);
    }

    // Deletes a specific user experience
    @DeleteMapping("/users/{userId}/experience/{experienceId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "experienceId") Long experienceId,
                                        @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return experienceService.deleteById(experienceId, currentUser);
    }

}
