package com.ted.service;

import com.ted.exception.NotAuthorizedException;
import com.ted.exception.ResourceNotFoundException;
import com.ted.model.Occupation;
import com.ted.model.User;
import com.ted.repository.OccupationRepository;
import com.ted.repository.UserRepository;
import com.ted.request.OccupationRequest;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OccupationService {

    private static final Logger logger = LoggerFactory.getLogger(OccupationService.class);

    @Autowired
    private OccupationRepository occupationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidatePathService validatePathService;

    // Adds an occupation for a user
    @Transactional
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

    // Deletes a specific user occupation
    @Transactional
    public ResponseEntity<?> deleteById(Long occupationId, UserDetailsImpl currentUser) {
        Occupation occupation = validatePathService.validatePathAndGetOccupation(occupationId);

        // Check if the occupation belongs to the current user
        if (currentUser.getId() != occupation.getUser().getId() && !currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        occupationRepository.delete(occupation);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted occupation."));
    }

}
