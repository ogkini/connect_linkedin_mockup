package com.ted.repository;

import com.ted.model.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Returns a post's likes
    @Query("select l from Like l where l.post.id = :postId")
    List<Like> getAllByPostId(@Param("postId") Long postId);

    // Returns a specific like of specific post of a specific user
    @Query("select l from Like l where l.id = :likeId and l.post.id = :postId and l.post.owner.id = :userId")
    Optional<Like> findByIdAndAndPostIdAndUserId(@Param("likeId") Long likeId,
            @Param("postId") Long postId, @Param("userId") Long userId);

}
