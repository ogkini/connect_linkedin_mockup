package com.ted.controller;

import com.ted.model.Like;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.LikeService;
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
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private RelationshipService relationshipService;

    // A user likes a post
    @PostMapping("/users/{userId}/posts/{postId}/likes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "postId") Long postId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // A user can like his own or a friend's post
        if (currentUser.getId() != userId && !relationshipService.areConnected(userId, currentUser.getId())) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Like like = likeService.create(userId, postId, currentUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{likeId}")
                .buildAndExpand(like.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Like Created.", like));
    }

    // A user deletes a like
    @DeleteMapping("/users/{userId}/posts/{postId}/likes/{likeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteById(@PathVariable(value = "userId") Long userId,
                                        @PathVariable(value = "postId") Long postId,
                                        @PathVariable(value = "likeId") Long likeId,
                                        @Valid @CurrentUser UserDetailsImpl currentUser) {
        return likeService.deleteById(likeId, postId, userId, currentUser);
    }

}
