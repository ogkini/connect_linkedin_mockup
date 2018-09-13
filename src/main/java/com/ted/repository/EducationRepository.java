package com.ted.repository;

import com.ted.model.Education;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {

    // Returns a user's education
    @Query("select e from Education e where e.user.id = :userId")
    List<Education> getAllByUserId(@Param("userId") Long userId);

}
