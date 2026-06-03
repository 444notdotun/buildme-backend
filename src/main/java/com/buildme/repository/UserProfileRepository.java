package com.buildme.repository;

import com.buildme.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserProfile Repository - Handles user profile data access
 * Follows Repository Pattern for clean abstraction of data layer
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findById(String id);
}

