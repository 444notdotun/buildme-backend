package com.buildme.service;

import com.buildme.entity.Challenge;
import com.buildme.repository.ChallengeRepository;
import com.buildme.util.BuildMeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ClaudeService claudeService;

    public Challenge getOrGenerateWeeklyChallenge(String level, List<String> concepts) {
        String weekKey = BuildMeUtils.getWeekKey();
        Optional<Challenge> existing = challengeRepository.findTopByTypeAndWeekKeyOrderByGeneratedAtDesc(
            Challenge.ChallengeType.WEEKLY, weekKey
        );
        if (existing.isPresent()) return existing.get();

        String questionsJson = claudeService.generateWeeklyChallenge(level, concepts);
        if (questionsJson == null) return null;

        Challenge challenge = Challenge.builder()
            .type(Challenge.ChallengeType.WEEKLY)
            .questionsJson(questionsJson)
            .weekKey(weekKey)
            .generatedAt(LocalDateTime.now())
            .build();
        return challengeRepository.save(challenge);
    }

    public Challenge getOrGenerateInterestOfWeek() {
        String weekKey = BuildMeUtils.getWeekKey();
        Optional<Challenge> existing = challengeRepository.findTopByTypeAndWeekKeyOrderByGeneratedAtDesc(
            Challenge.ChallengeType.INTEREST_OF_WEEK, weekKey
        );
        if (existing.isPresent()) return existing.get();

        String interestJson = claudeService.generateInterestOfWeek();
        if (interestJson == null) return null;

        Challenge challenge = Challenge.builder()
            .type(Challenge.ChallengeType.INTEREST_OF_WEEK)
            .questionsJson(interestJson)
            .weekKey(weekKey)
            .generatedAt(LocalDateTime.now())
            .build();
        return challengeRepository.save(challenge);
    }

    public boolean challengeExists(Challenge.ChallengeType type, String weekKey) {
        return challengeRepository.countByTypeAndWeekKey(type, weekKey) > 0;
    }

    public Challenge saveChallenge(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public List<Challenge> getByType(Challenge.ChallengeType type) {
        return challengeRepository.findByTypeOrderByGeneratedAtDesc(type);
    }
}

