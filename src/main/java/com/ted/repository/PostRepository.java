package com.ted.repository;

import com.ted.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Returns a user's posts
    @Query("select p from Post p where p.owner.id = :userId")
    List<Post> getAllByUserId(@Param("userId") Long userId);

}
