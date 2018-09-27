package com.ted.controller;

import com.ted.model.Post;
import com.ted.request.PostRequest;
import com.ted.response.ApiResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.PostService;
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
public class PostController {

    @Autowired
    private PostService postService;

    // A user creates a post
    @PostMapping("/users/{userId}/posts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody PostRequest postRequest,
                                    @PathVariable(value = "userId") Long userId,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        // Check if the logged in user is authorized to access the path
        if (currentUser.getId() != userId) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Post post = postService.create(userId, postRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{postId}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Post Created.", post));
    }

    // Returns a user's posts
    @GetMapping("/users/{userId}/posts")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<Post> getAll(@PathVariable(value = "userId") Long userId,
                             @Valid @CurrentUser UserDetailsImpl currentUser) {
        return postService.getAll(userId, currentUser);
    }

}
