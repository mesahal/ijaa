package com.ijaa.user.presenter.rest.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayTokenBlacklistClient {

    private final RestTemplate restTemplate;

    @Value("${gateway.url:http://localhost:8000}")
    private String gatewayUrl;

    public void blacklistToken(String token, String userId, String userType) {
        try {
            String url = gatewayUrl + "/api/v1/token/blacklist";
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("token", token);
            requestBody.put("userId", userId);
            requestBody.put("userType", userType);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            restTemplate.postForEntity(url, request, Map.class);
            log.info("Token blacklisted via gateway for user: {} (type: {})", userId, userType);
            
        } catch (Exception e) {
            log.error("Failed to blacklist token via gateway: {}", e.getMessage());
            // Don't throw exception to avoid breaking logout flow
        }
    }

    public void blacklistAllUserTokens(String userId, String userType) {
        try {
            String url = gatewayUrl + "/api/v1/token/blacklist-all";
            
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("userId", userId);
            requestBody.put("userType", userType);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            
            restTemplate.postForEntity(url, request, Map.class);
            log.info("All user tokens blacklisted via gateway for user: {} (type: {})", userId, userType);
            
        } catch (Exception e) {
            log.error("Failed to blacklist all user tokens via gateway: {}", e.getMessage());
            // Don't throw exception to avoid breaking logout flow
        }
    }
}
