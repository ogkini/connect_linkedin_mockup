package com.ted.service;

import com.ted.exception.NotAuthorizedException;
import com.ted.exception.ResourceNotFoundException;
import com.ted.model.Experience;
import com.ted.model.User;
import com.ted.repository.ExperienceRepository;
import com.ted.repository.UserRepository;
import com.ted.request.ExperienceRequest;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {

    private static final Logger logger = LoggerFactory.getLogger(ExperienceService.class);

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatePathService validatePathService;

    // Adds an experience for a user
    public Experience create(Long userId, ExperienceRequest experienceRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Experience experience = new Experience();

        experience.setUser(user);
        experience.setTitle(experienceRequest.getTitle());
        experience.setCompany(experienceRequest.getCompany());
        experience.setStartDate(experienceRequest.getStartDate());
        experience.setEndDate(experienceRequest.getEndDate());

        return experienceRepository.save(experience);
    }

    // Returns a user's experience
    public List<Experience> getAll(Long userId) {
        return experienceRepository.getAllByUserId(userId);
    }

    // Deletes a specific user experience
    public ResponseEntity<?> deleteById(Long experienceId, UserDetailsImpl currentUser) {
        Experience experience = validatePathService.validatePathAndGetExperience(experienceId);

        // Check if the experience belongs to the current user
        if (currentUser.getId() != experience.getUser().getId() && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        experienceRepository.delete(experience);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted experience."));
    }

}
