package com.ted.service;

import com.ted.model.Comment;
import com.ted.model.User;
import com.ted.model.Post;
import com.ted.request.NotificationRequest;
import com.ted.repository.CommentRepository;
import com.ted.repository.UserRepository;
import com.ted.repository.PostRepository;
import com.ted.request.CommentRequest;
import com.ted.exception.ResourceNotFoundException;
import com.ted.exception.NotAuthorizedException;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import static java.lang.Math.min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    // A user comments on a post
    public Comment create(Long userId, Long postId, UserDetailsImpl currentUser, CommentRequest commentRequest) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Comment comment = new Comment();

        comment.setPost(post);
        comment.setUser(user);
        comment.setText(commentRequest.getText());

        // Create a notification for the owner
        String action = "commented on your post";
        NotificationRequest notificationRequest = new NotificationRequest(user, action, post);
        notificationService.create(userId, notificationRequest);

        return commentRepository.save(comment);
    }

    // Returns the number of comments of a post
    public int getCommentsCount(Long postId) {
        return commentRepository.getAllByPostId(postId).size();
    }

    // A user deletes a comment
    public ResponseEntity<?> deleteById(Long commentId, Long postId, Long userId, UserDetailsImpl currentUser) {
        Comment comment = validatePathService.validatePathAndGetComment(commentId, postId, userId);

        // Check if the comment belongs to the current user
        if (currentUser.getId() != comment.getUser().getId()) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        commentRepository.delete(comment);

        return ResponseEntity.ok().body(new ApiResponse(true, "Successfully deleted comment."));
    }

}
