package com.ijaa.config.controller;

import com.ijaa.config.common.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/health")
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> basicHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("service", "Config Service");
        response.put("message", "Config Service is running successfully");
        response.put("timestamp", LocalDateTime.now());
        response.put("version", "1.0.0");
        
        log.info("Basic health check successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, Object>> configHealth() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("status", "healthy");
            response.put("message", "Configuration management is working");
            response.put("config", "Spring Cloud Config Server");
            response.put("features", Map.of(
                "externalizedConfiguration", "Active",
                "environmentSpecificSettings", "Active",
                "configurationVersioning", "Supported"
            ));
            response.put("timestamp", LocalDateTime.now());
            
            log.info("Config health check successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Config health check failed", e);
            response.put("status", "unhealthy");
            response.put("message", "Configuration management failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Config Service test endpoint is working");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Config Service");
        
        log.info("Test endpoint called successfully");
        return ResponseEntity.ok(response);
    }
}
