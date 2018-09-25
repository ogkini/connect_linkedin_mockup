package com.ted.controller;

import com.ted.model.Relationship;
import com.ted.request.RelationshipRequest;
import com.ted.service.RelationshipService;
import com.ted.response.ApiResponse;
import com.ted.response.NetworkResponse;
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
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    // A user sends a friend request to another user
    @PostMapping("/relationships")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody RelationshipRequest relationshipRequest,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        Relationship relationship = relationshipService.create(currentUser.getId(), relationshipRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{relationshipId}")
                .buildAndExpand(relationship.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Friend Request Sent.", relationship));
    }

    // Returns a user's connections, received friend requests and sent friend requests
    @GetMapping("/users/{userId}/network")
    @PreAuthorize("hasRole('USER')")
    public NetworkResponse getAll(@PathVariable(value = "userId") Long userId,
                                  @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            // Don't show the private staff to other users..
        }

        return relationshipService.getAll(userId);
    }

    // A user accepts a request
    @PutMapping("/relationships/{relationshipId}")
    @PreAuthorize("hasRole('USER')")
    public Relationship updateById(@PathVariable(value = "relationshipId") Long relationshipId,
                                   @Valid @CurrentUser UserDetailsImpl currentUser) {
        return relationshipService.updateById(relationshipId, currentUser);
    }

    // A user declines a request
    @DeleteMapping("/relationships/{relationshipId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteById(@PathVariable(value = "relationshipId") Long relationshipId,
                                        @Valid @CurrentUser UserDetailsImpl currentUser) {
        return relationshipService.deleteById(relationshipId, currentUser);
    }

}
