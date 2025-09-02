package com.ijaa.file_service.controller;

import com.ijaa.file_service.repository.EventBannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthController {

    private final EventBannerRepository eventBannerRepository;

    @GetMapping("/event-banner")
    public ResponseEntity<Map<String, Object>> checkEventBannerHealth() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test database connection by counting event banners
            long count = eventBannerRepository.count();
            response.put("status", "healthy");
            response.put("message", "Event banner database connection successful");
            response.put("bannerCount", count);
            response.put("timestamp", System.currentTimeMillis());
            
            log.info("Event banner health check successful. Banner count: {}", count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Event banner health check failed", e);
            response.put("status", "unhealthy");
            response.put("message", "Event banner database connection failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "File Service is working");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
}
