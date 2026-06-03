package com.buildme.service;

import com.buildme.entity.SearchLog;
import com.buildme.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Search Service - Business logic for search operations (jobs, scholarships, open source)
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final ClaudeService claudeService;
    private final OpportunityService opportunityService;
    private final OpenSourceService openSourceService;
    private final NotificationService notificationService;
    private final SearchLogRepository searchLogRepository;

    /**
     * Search jobs
     */
    public void searchJobs() {
        try {
            String result = claudeService.searchJobs();
            logSearch("JOBS", result != null, result);
            notificationService.createNotification("JOB_SEARCH_COMPLETE", "Job search complete");
        } catch (Exception e) {
            log.error("Job search failed: {}", e.getMessage());
            logSearch("JOBS", false, e.getMessage());
        }
    }

    /**
     * Search scholarships
     */
    public void searchScholarships() {
        try {
            String result = claudeService.searchScholarships();
            logSearch("SCHOLARSHIPS", result != null, result);
            notificationService.createNotification("SCHOLARSHIP_SEARCH_COMPLETE", "Scholarship search complete");
        } catch (Exception e) {
            log.error("Scholarship search failed: {}", e.getMessage());
            logSearch("SCHOLARSHIPS", false, e.getMessage());
        }
    }

    /**
     * Search open source
     */
    public void searchOpenSource() {
        try {
            String result = claudeService.searchOpenSource();
            logSearch("OPEN_SOURCE", result != null, result);
            notificationService.createNotification("OPEN_SOURCE_SEARCH_COMPLETE", "Open source search complete");
        } catch (Exception e) {
            log.error("Open source search failed: {}", e.getMessage());
            logSearch("OPEN_SOURCE", false, e.getMessage());
        }
    }

    /**
     * Log search operation
     */
    private void logSearch(String type, boolean success, String result) {
        try {
            SearchLog log = SearchLog.builder()
                .searchType(type)
                .success(success)
                .resultsFound(success ? 1 : 0)
                .newResults(0)
                .errorMessage(success ? null : result)
                .searchedAt(LocalDateTime.now())
                .build();
            searchLogRepository.save(log);
        } catch (Exception e) {
            log.warn("Could not save search log: {}", e.getMessage());
        }
    }
}

