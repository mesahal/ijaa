package com.ijaa.file.controller;

import com.ijaa.file.common.annotation.RequiresFeature;
import com.ijaa.file.common.utils.AppUtils;
import com.ijaa.file.repository.EventBannerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(AppUtils.BASE_URL + "/health")
@Tag(name = "Health Controllers")
@RequiredArgsConstructor
public class HealthController {

    private final EventBannerRepository eventBannerRepository;

    @GetMapping("/status")
    @RequiresFeature("system.health")
    @Operation(summary = "Basic Health Check", description = "Check if the File Service is running")
    public ResponseEntity<Map<String, Object>> basicHealth() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", "healthy");
            response.put("service", "File Service");
            response.put("message", "File Service is running successfully");
            response.put("timestamp", LocalDateTime.now());
            response.put("version", "1.0.0");
            response.put("javaVersion", System.getProperty("java.version"));
            response.put("springVersion", "3.x");
            
            log.info("Basic health check successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Basic health check failed", e);
            response.put("status", "unhealthy");
            response.put("message", "Basic health check failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/database")
    @RequiresFeature("system.health")
    @Operation(summary = "Database Health Check", description = "Check database connectivity and basic operations")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test database connection by counting event banners
            long bannerCount = eventBannerRepository.count();
            
            response.put("status", "healthy");
            response.put("message", "Database connection successful");
            response.put("database", "PostgreSQL");
            response.put("metrics", Map.of(
                "eventBanners", bannerCount
            ));
            response.put("timestamp", LocalDateTime.now());
            
            log.info("Database health check successful. Banner count: {}", bannerCount);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Database health check failed", e);
            response.put("status", "unhealthy");
            response.put("message", "Database connection failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(500).body(response);
        }
    }
}
