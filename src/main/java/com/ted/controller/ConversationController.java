package com.ted.controller;

import com.ted.model.Conversation;
import com.ted.model.Message;
import com.ted.request.ConversationRequest;
import com.ted.request.MessageRequest;
import com.ted.response.ApiResponse;
import com.ted.response.NetworkResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.ConversationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class ConversationController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @Autowired
    private ConversationService conversationService;

    // A user initiates a conversation with another user
    @PostMapping("/conversations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(@Valid @RequestBody ConversationRequest conversationRequest,
                                    @Valid @CurrentUser UserDetailsImpl currentUser) {
        Conversation conversation = conversationService.create(currentUser.getId(), conversationRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{conversationId}")
                .buildAndExpand(conversation.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Conversation Started.", conversation));
    }

    // Returns a user's conversations
    @GetMapping("/conversations")
    @PreAuthorize("hasRole('USER')")
    public List<Conversation> getAll(@Valid @CurrentUser UserDetailsImpl currentUser) {
        return conversationService.getAll(currentUser.getId());
    }

    // A user sends a message in a conversation
    @PostMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createMessage(@Valid @RequestBody MessageRequest messageRequest,
                                           @PathVariable(value = "conversationId") Long conversationId,
                                           @Valid @CurrentUser UserDetailsImpl currentUser) {
        Message message = conversationService.createMessage(currentUser.getId(), conversationId, messageRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{messageId}")
                .buildAndExpand(message.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "Message Sent.", message));
    }

}
