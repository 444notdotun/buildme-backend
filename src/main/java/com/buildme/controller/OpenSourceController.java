package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.entity.OpenSource;
import com.buildme.service.OpenSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/open-source")
@RequiredArgsConstructor
public class OpenSourceController {

    private final OpenSourceService openSourceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OpenSource>>> list(@RequestParam(defaultValue = "false") boolean seen) {
        return ResponseEntity.ok(ApiResponse.success(openSourceService.getBySeen(seen)));
    }

    @PutMapping("/{id}/seen")
    public ResponseEntity<ApiResponse<Map<String,String>>> markSeen(@PathVariable String id) {
        openSourceService.markAsSeen(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "Marked as seen")));
    }
}

