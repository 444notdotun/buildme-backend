package com.buildme.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

/**
 * UserProfile Entity - Represents the single user profile
 */
@Entity
@Table(name = "user_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private String id;

    private String name;
    private String fullName;
    private String handle;
    private String github;
    private String level;
    private String targetLevel;
    private int phase;
    private int cheveningHours;
    private int cheveningTarget;

    @Column(columnDefinition = "TEXT")
    private String stackJson;

    @Column(columnDefinition = "TEXT")
    private String goalsJson;

    @Column(columnDefinition = "TEXT")
    private String jobProfilesJson;

    @Column(columnDefinition = "TEXT")
    private String pinHash;

    private int failedPinAttempts;
    private boolean pinLocked;
    private LocalDateTime lockUntil;
    private boolean firstLogin;

    private LocalDateTime lastUpdated;
}

