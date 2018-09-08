package com.ted.repository;

import com.ted.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Returns a user with a specific email
    Optional<User> findByEmail(String email);

    // Returns all users
    @Query("from User user")
    List<User> getAllUsers();

}
