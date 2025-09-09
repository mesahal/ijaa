package com.ijaa.user.presenter.rest.api;

import com.ijaa.user.common.utils.AppUtils;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.repository.ExperienceRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ConnectionRepository;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.repository.AdminRepository;
import com.ijaa.user.repository.AnnouncementRepository;
import com.ijaa.user.repository.ReportRepository;
import com.ijaa.user.repository.UserSettingsRepository;
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

@RestController
@RequestMapping(AppUtils.BASE_URL + "/health")
@Tag(name = "Health Controllers")
@RequiredArgsConstructor
@Slf4j
public class HealthController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ExperienceRepository experienceRepository;
    private final InterestRepository interestRepository;
    private final ConnectionRepository connectionRepository;
    private final FeatureFlagRepository featureFlagRepository;
    private final AdminRepository adminRepository;
    private final AnnouncementRepository announcementRepository;
    private final ReportRepository reportRepository;
    private final UserSettingsRepository userSettingsRepository;

    @GetMapping("/status")
    @Operation(summary = "Basic Health Check", description = "Check if the User Service is running")
    public ResponseEntity<Map<String, Object>> basicHealth() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", "healthy");
            response.put("service", "User Service");
            response.put("message", "User Service is running successfully");
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
    @Operation(summary = "Database Health Check", description = "Check database connectivity and basic operations")
    public ResponseEntity<Map<String, Object>> databaseHealth() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test database connection by counting entities
            long userCount = userRepository.count();
            long profileCount = profileRepository.count();
            long experienceCount = experienceRepository.count();
            long interestCount = interestRepository.count();
            long connectionCount = connectionRepository.count();
            long featureFlagCount = featureFlagRepository.count();
            long adminCount = adminRepository.count();
            long announcementCount = announcementRepository.count();
            long reportCount = reportRepository.count();
            long userSettingsCount = userSettingsRepository.count();
            
            response.put("status", "healthy");
            response.put("message", "Database connection successful");
            response.put("database", "PostgreSQL");
            response.put("metrics", Map.of(
                "users", userCount,
                "profiles", profileCount,
                "experiences", experienceCount,
                "interests", interestCount,
                "connections", connectionCount,
                "featureFlags", featureFlagCount,
                "admins", adminCount,
                "announcements", announcementCount,
                "reports", reportCount,
                "userSettings", userSettingsCount
            ));
            response.put("timestamp", LocalDateTime.now());
            
            log.info("Database health check successful. Users: {}, Profiles: {}, Experiences: {}, Interests: {}, Connections: {}, FeatureFlags: {}, Admins: {}, Announcements: {}, Reports: {}, UserSettings: {}", 
                    userCount, profileCount, experienceCount, interestCount, connectionCount, featureFlagCount, adminCount, announcementCount, reportCount, userSettingsCount);
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
