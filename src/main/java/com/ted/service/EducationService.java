package com.ted.service;

import com.ted.model.Education;
import com.ted.model.User;
import com.ted.repository.EducationRepository;
import com.ted.repository.UserRepository;
import com.ted.request.EducationRequest;
import com.ted.exception.ResourceNotFoundException;

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

}
