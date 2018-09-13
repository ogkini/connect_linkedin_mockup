package com.ted.service;

import com.ted.model.Experience;
import com.ted.model.User;
import com.ted.repository.ExperienceRepository;
import com.ted.repository.UserRepository;
import com.ted.request.ExperienceRequest;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(ExperienceService.class);

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

}
