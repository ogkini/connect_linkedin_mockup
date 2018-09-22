package com.ted.service;

import com.ted.model.User;
import com.ted.model.Experience;
import com.ted.model.Education;
import com.ted.repository.UserRepository;
import com.ted.repository.RelationshipRepository;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Returns all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Returns a specific user.
    public User getById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Sort the experience list in chronological order
        Collections.sort(user.getExperience(), new Comparator<Experience>(){
            public int compare(Experience e1, Experience e2) {
                return e2.getEndDate().compareTo(e1.getEndDate());
            }
        });

        // Sort the education list in chronological order
        Collections.sort(user.getEducation(), new Comparator<Education>(){
            public int compare(Education e1, Education e2) {
                return e2.getEndDate().compareTo(e1.getEndDate());
            }
        });

        // Get the pending friend requests
        user.setFriendRequests(relationshipRepository.getReceivedRequestsByUserId(userId).size());

        return user;
    }

}
