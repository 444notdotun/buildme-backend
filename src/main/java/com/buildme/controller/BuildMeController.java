//package com.buildme.controller;
//
//import com.buildme.dto.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.*;
//import java.util.*;
//
//@RestController
//@RequestMapping("/api")
//@CrossOrigin(origins = "*")
//@Slf4j
//@RequiredArgsConstructor
//public class BuildMeController {
//
//    // Services
//        package com.buildme.controller;
//
//        import com.buildme.dto.ApiResponse;
//        import lombok.RequiredArgsConstructor;
//        import org.springframework.http.ResponseEntity;
//        import org.springframework.web.bind.annotation.GetMapping;
//        import org.springframework.web.bind.annotation.RestController;
//
//        import java.time.LocalDateTime;
//        import java.util.Map;
//
//        @RestController
//        @RequiredArgsConstructor
//        public class BuildMeController {
//
//            @GetMapping("/api/health")
//            public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
//                return ResponseEntity.ok(ApiResponse.success(Map.of(
//                    "status", "ok",
//                    "time", LocalDateTime.now().toString(),
//                    "version", "1.0.0"
//                )));
//            }
//        }
