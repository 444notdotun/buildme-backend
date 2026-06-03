package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.dto.ProgressLogRequest;
import com.buildme.entity.ProgressLog;
import com.buildme.service.ProgressService;
import com.buildme.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;
    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String,Object>>> logProgress(@RequestBody ProgressLogRequest request) {
        progressService.logProgress(request.getShipped(), request.getEnergy(), request.getMinutes(), request.getConcept());
        int cheveningAdded = userProfileService.hasActiveJob() ? 8 : 0;
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "Progress logged", "cheveningHoursAdded", cheveningAdded)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProgressLog>>> getProgress(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ApiResponse.success(progressService.getRecent(limit)));
    }
}

