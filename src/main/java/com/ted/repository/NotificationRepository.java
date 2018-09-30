package com.ted.repository;

import com.ted.model.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Returns a user's new notifications
    @Query("select n from Notification n where n.user.id = :userId and n.seen = false")
    List<Notification> getAllByUserId(@Param("userId") Long userId);

}
