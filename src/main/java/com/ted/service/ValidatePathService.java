package com.ted.service;

import com.ted.model.Experience;
import com.ted.model.Education;
import com.ted.model.Relationship;
import com.ted.model.Like;
import com.ted.model.Comment;
import com.ted.repository.ExperienceRepository;
import com.ted.repository.EducationRepository;
import com.ted.repository.RelationshipRepository;
import com.ted.repository.LikeRepository;
import com.ted.repository.CommentRepository;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ValidatePathService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    private static final Logger logger = LoggerFactory.getLogger(ValidatePathService.class);

    // Returns an experience if the path is valid
    public Experience validatePathAndGetExperience(Long experienceId) {
        return experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience", "id", experienceId));
    }

    // Returns an education if the path is valid
    public Education validatePathAndGetEducation(Long educationId) {
        return educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "id", educationId));
    }

    // Returns a relationship if the path is valid
    public Relationship validatePathAndGetRelationship(Long relationshipId) {
        return relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Relationship", "id", relationshipId));
    }

    // Returns a like if the path is valid
    public Like validatePathAndGetLike(Long likeId, Long postId, Long userId) {
        return likeRepository.findByIdAndAndPostIdAndUserId(likeId, postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Like", "id", likeId));
    }

    // Returns a comment if the path is valid
    public Comment validatePathAndGetComment(Long commentId, Long postId, Long userId) {
        return commentRepository.findByIdAndAndPostIdAndUserId(commentId, postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }

}
