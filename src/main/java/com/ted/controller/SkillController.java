package com.ted.controller;

import com.ted.model.Skill;
import com.ted.request.SkillRequest;
import com.ted.service.SkillService;
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
public class SkillController {

    @Autowired
    private SkillService skillService;

    // Adds a skill for a user
    @PostMapping("/users/{userId}/skills")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody SkillRequest skillRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Skill skill = skillService.create(userId, skillRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{skillId}")
                .buildAndExpand(skill.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Skill Created.", skill));
    }

    // Returns a user's skills
    @GetMapping("/users/{userId}/skills")
    @PreAuthorize("hasRole('USER')")
    public List<Skill> getAll(@PathVariable(value = "userId") Long userId,
                              @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        return skillService.getAll(userId);
    }

}
