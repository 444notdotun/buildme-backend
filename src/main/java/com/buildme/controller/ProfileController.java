package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.entity.UserProfile;
import com.buildme.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfile>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(userProfileService.getOrInitialize()));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateProfile(@RequestBody UserProfile updates) {
        userProfileService.updateProfile(updates);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

