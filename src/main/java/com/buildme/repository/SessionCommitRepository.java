package com.buildme.repository;

import com.buildme.entity.SessionCommit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionCommitRepository extends JpaRepository<SessionCommit, String> {
    Optional<SessionCommit> findTopByOrderByGeneratedAtDesc();
}