package com.ted.service;

import com.ted.exception.ResourceNotFoundException;
import com.ted.model.Occupation;
import com.ted.model.User;
import com.ted.repository.OccupationRepository;
import com.ted.repository.UserRepository;
import com.ted.request.OccupationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OccupationService {

    private static final Logger logger = LoggerFactory.getLogger(OccupationService.class);

    @Autowired
    private OccupationRepository occupationRepository;

    @Autowired
    private UserRepository userRepository;

    // Adds an occupation for a user
    public Occupation create(Long userId, OccupationRequest occupationRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Occupation occupation = new Occupation();

        occupation.setUser(user);
        occupation.setTitle(occupationRequest.getTitle());
        occupation.setCompany(occupationRequest.getCompany());

        return occupationRepository.save(occupation);
    }

    // Returns a user's occupation
    public Occupation get(Long userId) {
        return occupationRepository.getByUserId(userId).get();
    }

}
