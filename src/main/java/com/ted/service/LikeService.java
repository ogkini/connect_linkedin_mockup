package com.ted.service;

import com.ted.model.Like;
import com.ted.model.User;
import com.ted.model.Post;
import com.ted.request.NotificationRequest;
import com.ted.repository.LikeRepository;
import com.ted.repository.UserRepository;
import com.ted.repository.PostRepository;
import com.ted.service.NotificationService;
import com.ted.exception.ResourceNotFoundException;
import com.ted.exception.NotAuthorizedException;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);

    // A user likes a post
    @Transactional
    public Like create(Long userId, Long postId, UserDetailsImpl currentUser) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Like like = new Like();

        like.setPost(post);
        like.setUser(user);

        // Create a notification for the owner
        String action = "liked your post";
        NotificationRequest notificationRequest = new NotificationRequest(user, action, post);
        notificationService.create(userId, notificationRequest);

        return likeRepository.save(like);
    }

    // Returns the number of likes of a post
    public int getLikesCount(Long postId) {
        return likeRepository.getAllByPostId(postId).size();
    }

    // A user deletes a like
    @Transactional
    public ResponseEntity<?> deleteById(Long likeId, Long postId, Long userId, UserDetailsImpl currentUser) {
        Like like = validatePathService.validatePathAndGetLike(likeId, postId, userId);

        // Check if the like belongs to the current user
        if (currentUser.getId() != like.getUser().getId()) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        likeRepository.delete(like);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted like."));
    }

}
