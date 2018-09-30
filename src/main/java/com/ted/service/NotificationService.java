package com.ted.service;

import com.ted.model.Notification;
import com.ted.model.User;
import com.ted.model.Post;
import com.ted.repository.NotificationRepository;
import com.ted.repository.UserRepository;
import com.ted.repository.PostRepository;
import com.ted.request.NotificationRequest;
import com.ted.response.ApiResponse;
import com.ted.security.UserDetailsImpl;
import com.ted.exception.NotAuthorizedException;
import com.ted.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    // Creates a notification for a user
    public Notification create(Long userId, NotificationRequest notificationRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Notification notification = new Notification();

        notification.setUser(user);
        notification.setFrom(notificationRequest.getFrom());
        notification.setAction(notificationRequest.getAction());
        notification.setPost(notificationRequest.getPost());
        notification.setSeen(false);

        return notificationRepository.save(notification);
    }

    // Returns a user's notification
    public List<Notification> getAll(Long userId) {
        List<Notification> notifications = notificationRepository.getAllByUserId(userId);

        // Update the notifications as seen.
        for (Notification n : notifications) {
            n.setSeen(true);
            notificationRepository.save(n);
        }

        return notifications;
    }

}
