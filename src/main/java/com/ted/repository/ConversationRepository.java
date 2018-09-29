package com.ted.repository;

import com.ted.model.Conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // Returns a user's conversations
    @Query("select c from Conversation c where c.userOne.id = :userId or c.userTwo.id = :userId")
    List<Conversation> getByUserId(@Param("userId") Long userId);

}
