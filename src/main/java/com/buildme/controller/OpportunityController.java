package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.entity.Opportunity;
import com.buildme.service.OpportunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService opportunityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Opportunity>>> list(@RequestParam(defaultValue = "false") boolean seen,
                                                                @RequestParam(required = false) String type) {
        return ResponseEntity.ok(ApiResponse.success(opportunityService.getBySeenStatus(seen)));
    }

    @PutMapping("/{id}/seen")
    public ResponseEntity<ApiResponse<Map<String,String>>> markSeen(@PathVariable String id) {
        opportunityService.markAsSeen(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "Marked as seen")));
    }
}

