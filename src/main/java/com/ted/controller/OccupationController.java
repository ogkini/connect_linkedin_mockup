package com.ted.controller;

import com.ted.model.Occupation;
import com.ted.request.OccupationRequest;
import com.ted.service.OccupationService;
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

@RestController
@RequestMapping("/api")
public class OccupationController {

    @Autowired
    private OccupationService occupationService;

    // Adds an occupation for a user
    @PostMapping("/users/{userId}/occupation")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody OccupationRequest occupationRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Occupation occupation = occupationService.create(userId, occupationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand().toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Occupation Created.", occupation));
    }

    // Returns a user's occupation
    @GetMapping("/users/{userId}/occupation")
    @PreAuthorize("hasRole('USER')")
    public Occupation get(@PathVariable(value = "userId") Long userId,
                          @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return occupationService.get(userId);
    }
}
