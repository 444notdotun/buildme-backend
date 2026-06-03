package com.buildme.controller;

import com.buildme.dto.ApiResponse;
import com.buildme.entity.Notification;
import com.buildme.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Notification>>> getNotifications(@RequestParam(defaultValue = "false") boolean read) {
        return ResponseEntity.ok(ApiResponse.success(read ? notificationService.getRead() : notificationService.getUnread()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Map<String,String>>> markRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "Notification marked read")));
    }
}

