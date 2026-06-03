package com.buildme.scheduler;

import com.buildme.entity.Challenge;
import com.buildme.entity.SessionCommit;
import com.buildme.service.*;
import com.buildme.util.BuildMeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.*;

/**
 * BuildMe Scheduler - Handles automated tasks
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class BuildMeScheduler {

    private final ChallengeService challengeService;
    private final SearchService searchService;
    private final SessionCommitService sessionCommitService;
    private final NotificationService notificationService;
    private final UserProfileService userProfileService;
    private final ProgressService progressService;

    @Value("${app.scheduling.enabled:true}")
    private boolean schedulingEnabled;

    @Scheduled(fixedRateString = "${app.search.interval:18000000}")
    public void searchOpportunities() {
        if (!schedulingEnabled) return;
        log.info("[SCHEDULER] Running opportunity search — {}", LocalDateTime.now());
        searchService.searchJobs();
        searchService.searchScholarships();
        searchService.searchOpenSource();
        log.info("[SCHEDULER] Opportunity search complete");
    }

    @Scheduled(cron = "0 0 8 * * MON", zone = "Africa/Lagos")
    public void generateWeeklyChallenge() {
        if (!schedulingEnabled) return;
        log.info("[SCHEDULER] Generating weekly challenge — {}", LocalDate.now());
        try {
            String level = userProfileService.getUserLevel();
            List<String> concepts = progressService.getRecentConcepts();
            Challenge challenge = challengeService.getOrGenerateWeeklyChallenge(level, concepts);
            if (challenge != null) {
                notificationService.createNotification(
                    "WEEKLY_CHALLENGE_READY",
                    "Your weekly challenge is ready. No notes."
                );
                log.info("[SCHEDULER] Weekly challenge generated");
            }
        } catch (Exception e) {
            log.error("[SCHEDULER] Failed to generate weekly challenge: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 9 1 * *", zone = "Africa/Lagos")
    public void flagMonthlyReflection() {
        if (!schedulingEnabled) return;
        log.info("[SCHEDULER] Flagging monthly reflection — {}", LocalDate.now());
        notificationService.createNotification(
            "MONTHLY_REFLECTION_DUE",
            "Monthly reflection is due. Cumulative. Everything this month. No notes."
        );
    }

    @Scheduled(cron = "0 0 9 1 1,4,7,10 *", zone = "Africa/Lagos")
    public void flagEraReview() {
        if (!schedulingEnabled) return;
        log.info("[SCHEDULER] Flagging era review — {}", LocalDate.now());
        notificationService.createNotification(
            "ERA_REVIEW_DUE",
            "3-month era review is due. Everything at this stage. No notes. No hints."
        );
    }

    @Scheduled(cron = "0 0 20 * * *", zone = "Africa/Lagos")
    public void autoGenerateEodCommit() {
        if (!schedulingEnabled) return;
        log.info("[SCHEDULER] Auto-generating EOD commit — {}", LocalDate.now());
        try {
            List<String> recentProgress = progressService.getRecentProgress()
                .stream()
                .map(p -> p.getShipped())
                .toList();

            List<String> openLoops = List.of(
                "Ghost Coach: check progress",
                "Wallet system: not started",
                "HNG Stage 3: needs resubmission"
            );

            SessionCommit commit = sessionCommitService.generateCommit(
                recentProgress, openLoops, true
            );

            if (commit != null) {
                notificationService.createNotification(
                    "EOD_COMMIT_READY",
                    "Your end-of-day commit is ready. Copy the prompt for Claude."
                );
                log.info("[SCHEDULER] EOD commit generated");
            }
        } catch (Exception e) {
            log.error("[SCHEDULER] Failed to generate EOD commit: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 10 * * SUN", zone = "Africa/Lagos")
    public void generateInterestOfWeek() {
        if (!schedulingEnabled) return;
        log.info("[SCHEDULER] Generating interest of the week — {}", LocalDate.now());
        try {
            Challenge challenge = challengeService.getOrGenerateInterestOfWeek();
            if (challenge != null) {
                notificationService.createNotification(
                    "INTEREST_OF_WEEK_READY",
                    "This week's interesting topic is ready. Something outside your niche."
                );
            }
        } catch (Exception e) {
            log.error("[SCHEDULER] Failed to generate interest of week: {}", e.getMessage());
        }
    }
}
