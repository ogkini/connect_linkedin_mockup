package com.ted.controller;

import com.ted.model.JobOffer;
import com.ted.model.Post;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.JobOfferService;
import com.ted.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TimelineController {

    @Autowired
    private PostService postService;

    @Autowired
    private JobOfferService jobOfferService;

    // Returns a user's friends' posts
    @GetMapping("/users/{userId}/home")
    @PreAuthorize("hasRole('USER')")
    public List<Post> getNetworkPosts(@PathVariable(value = "userId") Long userId,
                          @Valid @CurrentUser UserDetailsImpl currentUser) {
        return postService.getNetworkPosts(userId, currentUser);
    }

    // Returns a user's friends' jobOffers
    @GetMapping("/users/{userId}/networkJobOffers")
    @PreAuthorize("hasRole('USER')")
    public List<JobOffer> getNetworkJobOffers(@PathVariable(value = "userId") Long userId,
                                          @Valid @CurrentUser UserDetailsImpl currentUser) {
        return jobOfferService.getNetworkJobOffers(userId, currentUser);
    }

}
