package com.ted.repository;

import com.ted.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Returns a user's unseen messages
    @Query("select m from Message m where m.receiver.id = :userId and m.seen = false")
    List<Message> getNewMessagesByUserId(@Param("userId") Long userId);

}
