package com.ted.repository;

import com.ted.model.Relationship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    // Returns a user's connections
    @Query("select r from Relationship r where (r.receiver.id = :userId or r.sender.id = :userId) and r.status = 1")
    List<Relationship> getConnectionsByUserId(@Param("userId") Long userId);

    // Returns a user's pending received requests
    @Query("select r from Relationship r where r.receiver.id = :userId and r.status = 0")
    List<Relationship> getReceivedRequestsByUserId(@Param("userId") Long userId);

    // Returns a user's new pending received requests
    @Query("select r from Relationship r where r.receiver.id = :userId and r.status = 0 and r.seen = false")
    List<Relationship> getNewReceivedRequestsByUserId(@Param("userId") Long userId);

    // Returns a user's pending sent requests
    @Query("select r from Relationship r where r.sender.id = :userId and r.status = 0")
    List<Relationship> getSentRequestsByUserId(@Param("userId") Long userId);

    // Checks if there is a relationship between two users
    @Query("select r from Relationship r where" +
            "(r.sender.id = :userOne and r.receiver.id = :userTwo) or" +
            "(r.sender.id = :userTwo and r.receiver.id = :userOne)")
    List<Relationship> areRelated(@Param("userOne") Long userOne, @Param("userTwo") Long userTwo);

}
