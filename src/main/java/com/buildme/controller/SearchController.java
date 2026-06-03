package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    @Autowired
    private  SearchService searchService;

    @PostMapping("/run")
    public ResponseEntity<ApiResponse<Map<String, String>>> runSearch() {
        searchService.searchJobs();
        searchService.searchScholarships();
        searchService.searchOpenSource();
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "Search started in background")));
    }
}

