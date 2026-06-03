package com.buildme.repository;

import com.buildme.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, String> {

    Optional<Challenge> findTopByTypeAndWeekKeyOrderByGeneratedAtDesc(
            Challenge.ChallengeType type,
            String weekKey
    );

    int countByTypeAndWeekKey(
            Challenge.ChallengeType type,
            String weekKey
    );

    List<Challenge> findByTypeOrderByGeneratedAtDesc(
            Challenge.ChallengeType type
    );
}