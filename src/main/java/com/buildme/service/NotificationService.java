package com.buildme.service;

import com.buildme.entity.Notification;
import com.buildme.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Notification Service - Business logic for notifications
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Create notification
     */
    public Notification createNotification(String type, String message) {
        Notification notification = Notification.builder()
            .type(type)
            .message(message)
            .read(false)
            .createdAt(LocalDateTime.now())
            .build();
        return notificationRepository.save(notification);
    }

    /**
     * Get unread notifications
     */
    public List<Notification> getUnread() {
        return notificationRepository.findRecent(false);
    }

    /**
     * Get read notifications
     */
    public List<Notification> getRead() {
        return notificationRepository.findRecent(true);
    }

    /**
     * Mark as read
     */
    public void markAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Get unread count
     */
    public int getUnreadCount() {
        return notificationRepository.countUnread();
    }
}

