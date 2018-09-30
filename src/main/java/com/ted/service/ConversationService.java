package com.ted.service;

import com.ted.model.Conversation;
import com.ted.model.Message;
import com.ted.model.User;
import com.ted.repository.ConversationRepository;
import com.ted.repository.MessageRepository;
import com.ted.request.ConversationRequest;
import com.ted.request.MessageRequest;
import com.ted.response.ApiResponse;
import com.ted.response.NetworkResponse;
import com.ted.exception.NotAuthorizedException;
import com.ted.exception.ResourceNotFoundException;
import com.ted.security.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    // A user initiates a conversation
    @Transactional
    public Conversation create(Long userId, ConversationRequest conversationRequest) {
        User me = userService.getById(userId);

        // Check that the request receiver exists
        User receiver = userService.getById(conversationRequest.getReceiver());

        Conversation conversation = new Conversation();

        conversation.setUserOne(me);
        conversation.setUserTwo(receiver);

        return conversationRepository.save(conversation);
    }

    // Returns a user's connections, received friend requests and sent friend requests
    public List<Conversation> getAll(Long userId) {
        return conversationRepository.getByUserId(userId);
    }

    // A user sends a message in a conversation
    @Transactional
    public Message createMessage(Long userId, Long conversationId, MessageRequest messageRequest) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));

        // Check if the user is one of the two members of the conversation
        if (userId != conversation.getUserOne().getId() && userId != conversation.getUserTwo().getId()) {
            throw new NotAuthorizedException("You are not authorized to create this resource.");
        }

        Message message = new Message();

        // The sender is always the user making the request.
        message.setSender(userService.getById(userId));

        // Get the receiver from the conversation object.
        // The receiver is the user that is not the current one.
        if (userId == conversation.getUserOne().getId()) {
            message.setReceiver(userService.getById(conversation.getUserTwo().getId()));
        } else {
            message.setReceiver(userService.getById(conversation.getUserOne().getId()));
        }

        message.setConversation(conversation);
        message.setText(messageRequest.getText());
        message.setSeen(false);
        message.setOpened(false);

        return messageRepository.save(message);
    }
}
