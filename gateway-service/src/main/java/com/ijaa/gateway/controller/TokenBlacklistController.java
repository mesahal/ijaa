package com.ijaa.gateway.controller;

import com.ijaa.gateway.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
@Slf4j
public class TokenBlacklistController {

    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/blacklist")
    public ResponseEntity<Map<String, String>> blacklistToken(
            @RequestBody Map<String, String> request) {
        
        String token = request.get("token");
        String userId = request.get("userId");
        String userType = request.get("userType");
        
        if (token == null || userId == null || userType == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Missing required fields: token, userId, userType"));
        }
        
        try {
            tokenBlacklistService.blacklistToken(token, userId, userType);
            return ResponseEntity.ok(Map.of("message", "Token blacklisted successfully"));
        } catch (Exception e) {
            log.error("Failed to blacklist token: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to blacklist token"));
        }
    }

    @PostMapping("/blacklist-all")
    public ResponseEntity<Map<String, String>> blacklistAllUserTokens(
            @RequestBody Map<String, String> request) {
        
        String userId = request.get("userId");
        String userType = request.get("userType");
        
        if (userId == null || userType == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Missing required fields: userId, userType"));
        }
        
        try {
            tokenBlacklistService.blacklistAllUserTokens(userId, userType);
            return ResponseEntity.ok(Map.of("message", "All user tokens blacklisted successfully"));
        } catch (Exception e) {
            log.error("Failed to blacklist all user tokens: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to blacklist all user tokens"));
        }
    }

    @GetMapping("/is-blacklisted")
    public ResponseEntity<Map<String, Object>> isTokenBlacklisted(
            @RequestParam String token) {
        
        try {
            boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(token);
            return ResponseEntity.ok(Map.of(
                "token", token,
                "isBlacklisted", isBlacklisted
            ));
        } catch (Exception e) {
            log.error("Failed to check token blacklist status: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Failed to check token blacklist status"));
        }
    }
}
