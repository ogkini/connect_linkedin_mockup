package com.ted.service;

import com.ted.model.Post;
import com.ted.model.User;
import com.ted.model.Relationship;
import com.ted.repository.PostRepository;
import com.ted.repository.UserRepository;
import com.ted.repository.RelationshipRepository;
import com.ted.request.PostRequest;
import com.ted.exception.ResourceNotFoundException;
import com.ted.exception.NotAuthorizedException;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // A user creates a post
    public Post create(Long userId, PostRequest postRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Post post = new Post();

        post.setOwner(user);
        post.setText(postRequest.getText());

        return postRepository.save(post);
    }

    // Returns a user's posts
    public List<Post> getAll(Long userId) {
        List<Post> posts = postRepository.getAllByUserId(userId);

        for (Post p : posts) {
            p.setLikesCount(likeService.getLikesCount(p.getId()));
        }

        return posts;
    }

    // Returns a user's friends' posts
    public List<Post> getNetworkPosts(Long userId) {
        List<Post> posts = new ArrayList<>();

        // Get the user's network
        List<Relationship> connections = relationshipRepository.getConnectionsByUserId(userId);

        // Get the posts of the specific users and add them to the list
        for (Relationship c : connections) {
            if (userId == c.getSender().getId()) {
                posts.addAll(getAll(c.getReceiver().getId()));
            } else {
                posts.addAll(getAll(c.getSender().getId()));
            }
        }

        return posts;
    }

}
