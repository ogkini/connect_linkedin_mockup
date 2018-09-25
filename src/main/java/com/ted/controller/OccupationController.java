package com.ted.controller;

import com.ted.exception.NotAuthorizedException;
import com.ted.model.Occupation;
import com.ted.request.OccupationRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.OccupationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class OccupationController {

    private static final Logger logger = LoggerFactory.getLogger(OccupationController.class);

    @Autowired
    private OccupationService occupationService;

    // Adds an occupation for a user
    @PostMapping("/users/{userId}/occupation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody OccupationRequest occupationRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Occupation get(@PathVariable(value = "userId") Long userId,
                          @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            // Don't show the private staff to other users..
            // Return null or empty Set ? --> Test how each case gets handled by the FrontEnd.
        }

        return occupationService.get(userId);
    }

}
