package com.ted.service;

import com.ted.model.User;
import com.ted.repository.UserRepository;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Returns all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Returns a specific user.
    public User getById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

}
