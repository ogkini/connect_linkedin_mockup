package com.ted.repository;

import com.ted.model.Skill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // Returns a user's skills
    @Query("select s from Skill s where s.user.id = :userId")
    List<Skill> getAllByUserId(@Param("userId") Long userId);

}
