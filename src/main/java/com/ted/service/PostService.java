package com.ted.service;

import com.ted.model.Post;
import com.ted.model.Like;
import com.ted.model.Comment;
import com.ted.model.User;
import com.ted.model.Relationship;
import com.ted.repository.PostRepository;
import com.ted.repository.UserRepository;
import com.ted.repository.RelationshipRepository;
import com.ted.repository.LikeRepository;
import com.ted.repository.CommentRepository;
import com.ted.request.PostRequest;
import com.ted.exception.ResourceNotFoundException;
import com.ted.exception.NotAuthorizedException;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

import static java.lang.Math.toIntExact;
import static java.lang.Math.ceil;

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
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ValidatePathService validatePathService;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    // A user creates a post
    @Transactional
    public Post create(Long userId, PostRequest postRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Post post = new Post();

        post.setOwner(user);
        post.setText(postRequest.getText());

        return postRepository.save(post);
    }

    // Returns a user's posts
    public List<Post> getAll(Long userId, UserDetailsImpl currentUser) {
        List<Post> posts = postRepository.getAllByUserId(userId);

        for (Post p : posts) {
            // Check the posts that the current user has liked
            for (Like l : p.getLikes()) {
                p.setLikesPost(currentUser.getId() == l.getUser().getId());
            }

            // Set the likes and comments counters
            p.setLikesCount(likeService.getLikesCount(p.getId()));
            p.setCommentsCount(commentService.getCommentsCount(p.getId()));
        }

        return posts;
    }

    // Returns a user's specific post
    @Transactional
    public Post getById(Long postId, UserDetailsImpl currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        // Check if the current user has liked the post
        for (Like l : post.getLikes()) {
            post.setLikesPost(currentUser.getId() == l.getUser().getId());
        }

        // Set the likes and comments counters
        post.setLikesCount(likeService.getLikesCount(postId));
        post.setCommentsCount(commentService.getCommentsCount(postId));

        return post;
    }

    // Returns a user's network's posts (including his own posts)
    public List<Post> getNetworkPosts(Long userId, UserDetailsImpl currentUser) {
        // Get the total number of users and posts
        int usersCount = userRepository.findAll().size();
        int postsCount = postRepository.findAll().size();

        if (postsCount == 0) return new ArrayList<>();

        // Create a 2d matrix with rows representing users and columns representing posts,
        // thus, creating a vector for every user. Default value is zero when created.
        int[][] scores = new int[usersCount][postsCount];
        getScores(scores, usersCount, postsCount);

        // An array to store the cosine similarities between the current user and every other user.
        // The user's similarity to himself is given a negative value.
        Double[] userSimilarities = new Double[usersCount];
        userSimilarities[toIntExact(currentUser.getId())-2] = -1.0;

        // Compute the similarities
        for (int i = 0; i < usersCount; i++) {
            // Skip the current user
            if (toIntExact(currentUser.getId()) == i+2) continue;

            userSimilarities[i] = cosineSimilarity(scores[toIntExact(currentUser.getId())-2], scores[i]);
        }

        // Sort the similarities array
        ArrayIndexComparator userComparator = new ArrayIndexComparator(userSimilarities);
        Integer[] userIndexes = userComparator.createIndexArray();
        Arrays.sort(userIndexes, userComparator);

        int k = 1; // Pick a value for the top k neighbors
        int p = 1; // Pick a value for the amount of system recommended posts

        // This array stores the scores of the posts that do not belong to someone connected with the user.
        Double[] recommendedPostScores = new Double[postsCount];
        Arrays.fill(recommendedPostScores, 0.0);

        for (int j = 0; j < postsCount; j++) {
            Long postId = new Long(j+1);
            Post post = getById(postId, currentUser);

            // Check if the post owner is connected with the current user or it's the user himself.
            // If not, estimate the score of the post as an average of the top k neighbors' scores.
            if (currentUser.getId() != post.getOwner().getId() &&
                !relationshipService.areConnected(currentUser.getId(), post.getOwner().getId())) {
                int sum = 0;

                for (int i = 0; i < k; i++) sum += scores[userIndexes[i]][j];
                int average = (int) ceil(sum / k);

                scores[toIntExact(currentUser.getId())-2][j] = average;
                recommendedPostScores[j] = new Double(average);
            }
        }

        // Sort the recommendedPostScores array.
        // The postIndexes array holds the indexes of the top recommended posts.
        // We will present the top p to the user according to the variable p defined above.
        ArrayIndexComparator postComparator = new ArrayIndexComparator(recommendedPostScores);
        Integer[] postIndexes = postComparator.createIndexArray();
        Arrays.sort(postIndexes, postComparator);

        // If no post with a score greater that zero was found do not recommend
        if (recommendedPostScores[postIndexes[0]] == 0.0) p = 0;

        List<Post> posts = new ArrayList<>();

        // Get the user's network
        List<Relationship> connections = relationshipRepository.getConnectionsByUserId(userId);

        // Get the posts of the specific users and add them to the list
        for (Relationship c : connections) {
            if (userId == c.getSender().getId()) {
                posts.addAll(getAll(c.getReceiver().getId(), currentUser));
            } else {
                posts.addAll(getAll(c.getSender().getId(), currentUser));
            }
        }

        // Add the users own posts to the list
        posts.addAll(getAll(userId, currentUser));

        // Sort the posts from newest to oldest
        Collections.sort(posts, new Comparator<Post>(){
            public int compare(Post p1, Post p2) {
                return p2.getCreatedTime().compareTo(p1.getCreatedTime());
            }
        });

        for (Post post : posts) {
            post.setIsRecommended(false);
        }

        // Insert the recommended posts into the list
        int recommendedIndex = 0;
        for (int i = 0; i < posts.size(); i++) {
            if (i>0 && i%3 == 0 && recommendedIndex < p) {
                Long id = new Long(postIndexes[recommendedIndex]+1);
                Post post = getById(id, currentUser);

                post.setIsRecommended(true);
                posts.add(i, post);
                recommendedIndex++;
            }
        }

        // If any posts were not inserted append them to the list
        if (recommendedIndex < p) {
            for (int j = recommendedIndex; j < p; j++) {
                Long id = new Long(postIndexes[j]+1);
                Post post = getById(id, currentUser);

                post.setIsRecommended(true);
                posts.add(post);
            }
        }

        return posts;
    }

    // For every post in the matrix finds the users that have liked or
    // commented on the post, and assigns an appropriate score to every cell.
    //
    // 0 means the user has not liked nor commented on a post
    // 1 means the user has liked or commented on a post
    // 2 means the user has both liked and commented on a post
    private void getScores(int[][] scores, int usersCount, int postsCount) {
        for (int j = 0; j < postsCount; j++) {
            // Get the likes and the comments of the current post
            List<Like> likes = likeRepository.getAllByPostId(new Long(j+1));
            List<Comment> comments = commentRepository.getAllByPostId(new Long(j+1));

            // Extract the user ids from the likes and comments and update the matrix cells.
            // The value (i + 2) represents the current users id.
            // We add one because the iterator starts from 0
            // and another one because the first id belongs to the admin.
            // Thus, real user ids start from 2.
            for (int i = 0; i < usersCount; i++) {
                for (Like like : likes) {
                    if (like.getUser().getId() == i+2) {
                        scores[i][j]++;
                    }
                }

                for (Comment comment : comments) {
                    if (comment.getUser().getId() == i+2) {
                        scores[i][j]++;
                    }
                }
            }
        }
    }

    // Returns the cosine similarity between two vectors
    public static Double cosineSimilarity(int[] vectorA, int[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        if (Math.sqrt(normA) * Math.sqrt(normB) == 0) {
            return new Double(0);
        } else if (Math.sqrt(normA) == 0) {
            return new Double(dotProduct / Math.sqrt(normB));
        } else if (Math.sqrt(normB) == 0) {
            return new Double(dotProduct / Math.sqrt(normA));
        } else {
            return new Double(dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
        }
    }

}

class ArrayIndexComparator implements Comparator<Integer> {
    private final Double[] array;

    public ArrayIndexComparator(Double[] array) {
        this.array = array;
    }

    public Integer[] createIndexArray() {
        Integer[] indexes = new Integer[array.length];

        for (int i = 0; i < array.length; i++) {
            indexes[i] = i;
        }

        return indexes;
    }

    @Override
    public int compare(Integer index1, Integer index2) {
        return array[index2].compareTo(array[index1]);
    }
}
