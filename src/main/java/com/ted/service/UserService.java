package com.ted.service;

import com.ted.model.User;
import com.ted.repository.UserRepository;

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
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

}
