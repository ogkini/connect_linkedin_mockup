package com.ted.service;

import com.ted.exception.NotAuthorizedException;
import com.ted.exception.ResourceNotFoundException;
import com.ted.model.Skill;
import com.ted.model.User;
import com.ted.repository.SkillRepository;
import com.ted.repository.UserRepository;
import com.ted.request.SkillRequest;
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
public class SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatePathService validatePathService;

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

    // Deletes a specific user skill
    public ResponseEntity<?> deleteById(Long skillId, UserDetailsImpl currentUser) {
        Skill skill = validatePathService.validatePathAndGetSkill(skillId);

        // Check if the skill belongs to the current user
        if (currentUser.getId() != skill.getUser().getId() && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        skillRepository.delete(skill);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted skill."));
    }

}
