package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.service.ChallengeService;
import com.buildme.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final NotificationService notificationService;
    private final ChallengeService challengeService;

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<Map<String,Object>>> today() {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Africa/Lagos"));
        int unread = notificationService.getUnreadCount();
        String weekKey = String.valueOf(today.getYear()) + "-W" + (today.getDayOfYear() / 7);
        boolean weeklyReady = challengeService.challengeExists(com.buildme.entity.Challenge.ChallengeType.WEEKLY, weekKey);
        boolean interestReady = challengeService.challengeExists(com.buildme.entity.Challenge.ChallengeType.INTEREST_OF_WEEK, weekKey);

        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "today", today.toString(),
            "dayOfWeek", today.getDayOfWeek().toString(),
            "time", now.toString(),
            "timeZone", "Africa/Lagos",
            "weeklyChallengeReady", weeklyReady,
            "interestOfWeekReady", interestReady,
            "unreadNotifications", unread,
            "weekKey", weekKey
        )));
    }
}

