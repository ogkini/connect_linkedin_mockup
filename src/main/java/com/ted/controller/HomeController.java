package com.ted.controller;

import com.ted.model.Post;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private PostService postService;

    // Returns a user's friends' posts
    @GetMapping("/users/{userId}/home")
    @PreAuthorize("hasRole('USER')")
    public List<Post> getNetworkPosts(@PathVariable(value = "userId") Long userId,
                          @Valid @CurrentUser UserDetailsImpl currentUser) {
        return postService.getNetworkPosts(userId);
    }

}
