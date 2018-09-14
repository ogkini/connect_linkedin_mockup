package com.ted.service;

import com.ted.model.Skill;
import com.ted.model.User;
import com.ted.repository.SkillRepository;
import com.ted.repository.UserRepository;
import com.ted.request.SkillRequest;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    // Adds a skill for a user
    public Skill create(Long userId, SkillRequest skillRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Skill skill = new Skill();

        skill.setUser(user);
        skill.setName(skillRequest.getName());
        skill.setStrength(skillRequest.getStrength());

        return skillRepository.save(skill);
    }

    // Returns a user's skill
    public List<Skill> getAll(Long userId) {
        return skillRepository.getAllByUserId(userId);
    }

}
