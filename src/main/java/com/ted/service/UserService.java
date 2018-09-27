package com.ted.service;

import com.ted.exception.ResourceNotFoundException;
import com.ted.model.Education;
import com.ted.model.Experience;
import com.ted.model.User;
import com.ted.repository.RelationshipRepository;
import com.ted.repository.UserRepository;
import com.ted.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private RelationshipService relationshipService;

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
        user.setNewFriendRequests(relationshipRepository.getNewReceivedRequestsByUserId(userId).size());

        return user;
    }

    // Returns a specific user.
    public User getById(Long userId, UserDetailsImpl currentUser) {
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
        user.setNewFriendRequests(relationshipRepository.getNewReceivedRequestsByUserId(userId).size());

        // Check if there is a relationship between the users
        if (userId != currentUser.getId()) {
            user.setRelationshipBetween(relationshipService.areRelated(userId, currentUser.getId()));
        }

        return user;
    }

    // Returns a specific user based on its email.
    public User getByEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        return user;
    }

    // Returns a list of users matching the parameters.
    public List<User> getBySearch(String firstName, String lastName) {

        List<User> users = userRepository.getAllRelated(firstName, lastName);

        return users;
    }

}
