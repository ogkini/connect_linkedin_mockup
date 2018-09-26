package com.ted.controller;

import com.ted.exception.NotAuthorizedException;
import com.ted.model.Skill;
import com.ted.request.SkillRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.SkillService;
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
import java.util.List;

@RestController
@RequestMapping("/api")
public class SkillController {

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    @Autowired
    private SkillService skillService;

    // Adds a skill for a user
    @PostMapping("/users/{userId}/skills")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody SkillRequest skillRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Skill> getAll(@PathVariable(value = "userId") Long userId,
                              @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if ( currentUser.getId() != userId && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            // Don't show the private staff to other users..
            // Return null or empty Set ? --> Test how each case gets handled by the FrontEnd.
        }

        return skillService.getAll(userId);
    }

}
