package com.ted.controller;

import com.ted.model.Comment;
import com.ted.request.CommentRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.CommentService;
import com.ted.service.RelationshipService;
import com.ted.exception.NotAuthorizedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RelationshipService relationshipService;

    // A user comments on a post
    @PostMapping("/users/{userId}/posts/{postId}/comments")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody CommentRequest commentRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "postId") Long postId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // A user can comment on his own or a friend's post
        if (currentUser.getId() != userId && !relationshipService.areConnected(userId, currentUser.getId())) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Comment comment = commentService.create(userId, postId, currentUser, commentRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{commentId}")
                .buildAndExpand(comment.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Comment Created.", comment));
    }

    // A user deletes a comment
    @DeleteMapping("/users/{userId}/posts/{postId}/comments/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteById(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "postId") Long postId,
                                        @PathVariable(value = "commentId") Long commentId,
                                        @Valid @CurrentUser UserDetailsImpl currentUser) {
        return commentService.deleteById(commentId, postId, userId, currentUser);
    }

}
