package com.buildme.repository;

import com.buildme.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    @Query("SELECT n FROM Notification n WHERE n.read = :read ORDER BY n.createdAt DESC LIMIT 20")
    List<Notification> findRecent(boolean read);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.read = false")
    int countUnread();
}

