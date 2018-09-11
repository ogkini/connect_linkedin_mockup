package com.ted.repository;

import com.ted.model.Occupation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OccupationRepository extends JpaRepository<Occupation, Long> {

    // Returns a user's occupation
    @Query("select o from Occupation o where o.user.id = :userId")
    Optional<Occupation> getByUserId(@Param("userId") Long userId);

}
