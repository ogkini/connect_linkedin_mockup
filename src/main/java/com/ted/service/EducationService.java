package com.ted.service;

import com.ted.model.Education;
import com.ted.model.User;
import com.ted.repository.EducationRepository;
import com.ted.repository.UserRepository;
import com.ted.request.EducationRequest;
import com.ted.exception.ResourceNotFoundException;
import com.ted.exception.NotAuthorizedException;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(EducationService.class);

    // Adds an education for a user
    public Education create(Long userId, EducationRequest educationRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Education education = new Education();

        education.setUser(user);
        education.setTitle(educationRequest.getTitle());
        education.setSchool(educationRequest.getSchool());
        education.setStartDate(educationRequest.getStartDate());
        education.setEndDate(educationRequest.getEndDate());

        return educationRepository.save(education);
    }

    // Returns a user's education
    public List<Education> getAll(Long userId) {
        return educationRepository.getAllByUserId(userId);
    }

    // Deletes a specific user education
    public ResponseEntity<?> deleteById(Long educationId, UserDetailsImpl currentUser) {
        Education education = validatePathService.validatePathAndGetEducation(educationId);

        // Check if the education belongs to the current user
        if (currentUser.getId() != education.getUser().getId()) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        educationRepository.delete(education);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted education."));
    }

}
