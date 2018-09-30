package com.ted.service;

import com.ted.model.*;
import com.ted.repository.*;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ValidatePathService {

    @Autowired
    private OccupationRepository occupationRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JobApplyRepository jobApplyRepository;

    private static final Logger logger = LoggerFactory.getLogger(ValidatePathService.class);

    // Returns occupation if the path is valid
    public Occupation validatePathAndGetOccupation(Long occupationId) {
        return occupationRepository.findById(occupationId)
                .orElseThrow(() -> new ResourceNotFoundException("Occupation", "id", occupationId));
    }

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

    // Returns a skill if the path is valid
    public Skill validatePathAndGetSkill(Long skillId) {
        return skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill", "id", skillId));
    }

    // Returns a relationship if the path is valid
    public Relationship validatePathAndGetRelationship(Long relationshipId) {
        return relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Relationship", "id", relationshipId));
    }

    // Returns a like if the path is valid
    public Like validatePathAndGetLike(Long likeId, Long postId, Long userId) {
        return likeRepository.findByIdAndPostIdAndUserId(likeId, postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Like", "id", likeId));
    }

    // Returns a comment if the path is valid
    public Comment validatePathAndGetComment(Long commentId, Long postId, Long userId) {
        return commentRepository.findByIdAndPostIdAndUserId(commentId, postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));
    }

    // Returns a jobApply if the path is valid
    public JobApply validatePathAndGetJobApply(Long jobApplyId, Long jobOfferId, Long userId) {
        return jobApplyRepository.findByIdAndJobOfferIdAndUserId(jobApplyId, jobOfferId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApply", "id", jobApplyId));
    }

}
