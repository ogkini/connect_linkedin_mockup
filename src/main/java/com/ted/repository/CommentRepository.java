package com.ted.repository;

import com.ted.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Returns a post's comments
    @Query("select c from Comment c where c.post.id = :postId order by c.createdTime asc")
    List<Comment> getAllByPostId(@Param("postId") Long postId);

    // Returns a specific comment of specific post of a specific user
    @Query("select c from Comment c where c.id = :commentId and c.post.id = :postId and c.post.owner.id = :userId")
    Optional<Comment> findByIdAndAndPostIdAndUserId(@Param("commentId") Long commentId,
            @Param("postId") Long postId, @Param("userId") Long userId);

}
