package com.buildme.service;

import com.buildme.dto.DsaEntryRequest;
import com.buildme.entity.DsaEntry;
import com.buildme.repository.DsaEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DSA Service - Business logic for Data Structures & Algorithms tracking
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DsaService {

    private final DsaEntryRepository dsaEntryRepository;

    /**
     * Log DSA problem solved
     */
    public DsaEntry logDsaProblem(DsaEntryRequest request) {
        DsaEntry entry = DsaEntry.builder()
            .problem(request.getProblem())
            .difficulty(request.getDifficulty())
            .timeComplexity(request.getTimeComplexity())
            .spaceComplexity(request.getSpaceComplexity())
            .pattern(request.getPattern())
            .solvedAt(LocalDate.now())
            .loggedAt(LocalDateTime.now())
            .attempts(1)
            .build();

        return dsaEntryRepository.save(entry);
    }

    /**
     * Get DSA statistics
     */
    public Map<String, Object> getDsaStats() {
        List<DsaEntry> total = dsaEntryRepository.findTop10ByOrderByLoggedAtDesc();
        int streak = getStreak30Days();
        List<DsaEntry> recent = dsaEntryRepository.findTop10ByOrderByLoggedAtDesc();

        return Map.of(
            "total", total,
            "streak", streak,
            "recent", recent
        );
    }

    /**
     * Get 30-day streak
     */
    public int getStreak30Days() {
        return Math.toIntExact(dsaEntryRepository.countBySolvedAtGreaterThanEqual(LocalDate.of(0, 0, 30)));
    }

    /**
     * Get 7-day streak
     */
    public int getStreak7Days() {
        return Math.toIntExact(dsaEntryRepository.count());
    }

    /**
     * Get total DSA count
     */
    public int getTotalCount() {
        return Math.toIntExact(dsaEntryRepository.count());
    }

    /**
     * Get recent DSA entries
     */
    public List<DsaEntry> getRecent() {
        return dsaEntryRepository.findTop10ByOrderByLoggedAtDesc();
    }
}

