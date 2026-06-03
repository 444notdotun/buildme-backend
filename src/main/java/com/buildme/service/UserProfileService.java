package com.buildme.service;

import com.buildme.entity.UserProfile;
import com.buildme.repository.UserProfileRepository;
import com.buildme.util.BuildMeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * UserProfile Service - Business logic for user profile operations
 * Follows Service Layer Pattern and Single Responsibility Principle
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private static final String DEFAULT_USER_ID = "dotman";

    /**
     * Get or initialize user profile
     */
    public UserProfile getOrInitialize() {
        try {
            return userProfileRepository.findById(DEFAULT_USER_ID).orElseGet(() -> {
                log.info("Initializing default profile for user: {}", DEFAULT_USER_ID);
                return initializeDefaultProfile();
            });
        } catch (Exception e) {
            log.error("Failed to get user profile: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve user profile", e);
        }
    }

    /**
     * Get user profile - assumes it exists
     */
    public UserProfile getProfile() {
        return userProfileRepository.findById(DEFAULT_USER_ID)
            .orElseThrow(() -> new RuntimeException("User profile not found"));
    }

    /**
     * Update user profile
     */
    public UserProfile updateProfile(UserProfile profile) {
        profile.setLastUpdated(LocalDateTime.now());
        return userProfileRepository.save(profile);
    }

    /**
     * Get user level
     */
    public String getUserLevel() {
        try {
            UserProfile profile = getProfile();
            return profile.getLevel() != null ? profile.getLevel() : "JUNIOR";
        } catch (Exception e) {
            return "JUNIOR";
        }
    }

    /**
     * Check if user has active job profile
     */
    public boolean hasActiveJob() {
        try {
            UserProfile profile = getProfile();
            String jobProfiles = profile.getJobProfilesJson();
            return jobProfiles != null && jobProfiles.contains("\"active\":true");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get Chevening hours
     */
    public int getCheveningHours() {
        try {
            UserProfile profile = getProfile();
            return profile.getCheveningHours();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Increment Chevening hours
     */
    public void incrementCheveningHours(int amount) {
        UserProfile profile = getProfile();
        profile.setCheveningHours(profile.getCheveningHours() + amount);
        profile.setLastUpdated(LocalDateTime.now());
        userProfileRepository.save(profile);
    }

    /**
     * Verify PIN
     */
    public boolean verifyPin(String pin) {
        UserProfile profile = getProfile();

        if (profile.isPinLocked()) {
            LocalDateTime now = LocalDateTime.now();
            if (profile.getLockUntil() != null && now.isBefore(profile.getLockUntil())) {
                return false;
            }
            // Unlock if time has passed
            profile.setPinLocked(false);
        }

        String hash = BuildMeUtils.simpleHash(pin);
        if (hash.equals(profile.getPinHash())) {
            profile.setFailedPinAttempts(0);
            profile.setPinLocked(false);
            userProfileRepository.save(profile);
            return true;
        }

        // Increment failed attempts
        profile.setFailedPinAttempts(profile.getFailedPinAttempts() + 1);
        if (profile.getFailedPinAttempts() >= 5) {
            profile.setPinLocked(true);
            profile.setLockUntil(LocalDateTime.now().plusMinutes(15));
        }
        userProfileRepository.save(profile);
        return false;
    }

    /**
     * Set new PIN
     */
    public void setPin(String pin) {
        UserProfile profile = getProfile();
        profile.setPinHash(BuildMeUtils.simpleHash(pin));
        profile.setFirstLogin(false);
        profile.setLastUpdated(LocalDateTime.now());
        userProfileRepository.save(profile);
    }

    /**
     * Initialize default user profile
     */
    private UserProfile initializeDefaultProfile() {
        UserProfile profile = UserProfile.builder()
            .id(DEFAULT_USER_ID)
            .name("Adedotun")
            .fullName("Adedotun (Dotman) Adewole Stephen")
            .handle("@notdotun_")
            .github("444notdotun")
            .level("JUNIOR")
            .targetLevel("MID_LEVEL")
            .phase(1)
            .cheveningHours(0)
            .cheveningTarget(2800)
            .stackJson("[\"Java\",\"Spring Boot\",\"Spring Security\",\"PostgreSQL\",\"Docker\",\"React\"]")
            .goalsJson("[\"Mid-level in 4-5 months\",\"Senior in 12 months\",\"Global remote in 24 months\",\"Masters abroad in 3-4 years\"]")
            .failedPinAttempts(0)
            .pinLocked(false)
            .firstLogin(true)
            .lastUpdated(LocalDateTime.now())
            .build();
        return userProfileRepository.save(profile);
    }
}

