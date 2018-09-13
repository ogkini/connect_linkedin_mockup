package com.ted.repository;

import com.ted.model.Experience;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    // Returns a user's exprerience
    @Query("select e from Experience e where e.user.id = :userId")
    List<Experience> getAllByUserId(@Param("userId") Long userId);

}
