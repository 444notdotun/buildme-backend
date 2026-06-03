package com.buildme.service;

import com.buildme.entity.ProgressLog;
import com.buildme.repository.ProgressLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProgressService {

    private final ProgressLogRepository progressLogRepository;
    private final UserProfileService userProfileService;

    public ProgressLog logProgress(String shipped, String energy, int minutes, String concept) {
        ProgressLog log = ProgressLog.builder()
                .date(LocalDate.now())
                .energyLevel(energy)
                .availableMinutes(minutes)
                .shipped(shipped)
                .loggedAt(LocalDateTime.now())
                .build();

        if (concept != null) {
            log.setShipped(concept);
        }

        ProgressLog saved = progressLogRepository.save(log);

        if (userProfileService.hasActiveJob()) {
            userProfileService.incrementCheveningHours(8);
        }

        return saved;
    }

    public List<ProgressLog> getRecent(int limit) {
        return progressLogRepository.findTopByOrderByLoggedAtDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<ProgressLog> getRecentProgress() {
        return progressLogRepository.findTop5ByLoggedAtGreaterThanEqualOrderByLoggedAtDesc(
                LocalDateTime.now().minusDays(3)
        );
    }

    public List<String> getRecentConcepts() {
        List<String> concepts = progressLogRepository.findTop10ByShippedIsNotNull()
                .stream()
                .map(ProgressLog::getShipped)
                .distinct()
                .collect(Collectors.toList());

        if (concepts.isEmpty()) {
            return List.of("JWT", "Spring Security", "@Transactional", "ACID", "Microservices");
        }
        return concepts;
    }
}