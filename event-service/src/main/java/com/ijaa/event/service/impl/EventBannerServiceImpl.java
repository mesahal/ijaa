package com.ijaa.event.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event.domain.dto.FileUploadResponse;
import com.ijaa.event.domain.dto.PhotoUrlResponse;
import com.ijaa.event.presenter.rest.client.FileServiceClient;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.EventBannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventBannerServiceImpl implements EventBannerService {

    private final FileServiceClient fileServiceClient;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public FileUploadResponse uploadBanner(String eventId, MultipartFile file, String username) {
        log.info("Uploading banner for event: {} by user: {}", eventId, username);

        // Verify event exists
        if (!eventRepository.existsById(Long.valueOf(eventId))) {
            throw new RuntimeException("Event not found");
        }

        // Get authorization token from current request
        String authToken = getAuthorizationToken();
        if (authToken == null) {
            throw new RuntimeException("Authorization token not available");
        }

        // Delegate to File Service
        Object response = fileServiceClient.uploadEventBanner(eventId, file, authToken);
        
        // Parse the response
        Map<String, Object> responseMap = objectMapper.convertValue(response, Map.class);
        
        if (!(Boolean) responseMap.get("success")) {
            throw new RuntimeException("File service error: " + responseMap.get("message"));
        }
        
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        return objectMapper.convertValue(data, FileUploadResponse.class);
    }

    @Override
    public PhotoUrlResponse getBannerUrl(String eventId) {
        log.info("Getting banner URL for event: {}", eventId);

        // Get authorization token from current request
        String authToken = getAuthorizationToken();
        if (authToken == null) {
            throw new RuntimeException("Authorization token not available");
        }

        // Delegate to File Service
        Object response = fileServiceClient.getEventBannerUrl(eventId, authToken);
        
        // Parse the response
        Map<String, Object> responseMap = objectMapper.convertValue(response, Map.class);
        
        if (!(Boolean) responseMap.get("success")) {
            throw new RuntimeException("File service error: " + responseMap.get("message"));
        }
        
        Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
        return objectMapper.convertValue(data, PhotoUrlResponse.class);
    }

    @Override
    public void deleteBanner(String eventId, String username) {
        log.info("Deleting banner for event: {} by user: {}", eventId, username);

        // Verify event exists
        if (!eventRepository.existsById(Long.valueOf(eventId))) {
            throw new RuntimeException("Event not found");
        }

        // Get authorization token from current request
        String authToken = getAuthorizationToken();
        if (authToken == null) {
            throw new RuntimeException("Authorization token not available");
        }

        // Delegate to File Service
        Object response = fileServiceClient.deleteEventBanner(eventId, authToken);
        
        // Parse the response
        Map<String, Object> responseMap = objectMapper.convertValue(response, Map.class);
        
        if (!(Boolean) responseMap.get("success")) {
            throw new RuntimeException("File service error: " + responseMap.get("message"));
        }
    }

    /**
     * Get the Authorization token from the current request
     */
    private String getAuthorizationToken() {
        try {
            jakarta.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader("Authorization");
            return authHeader != null ? authHeader : null;
        } catch (Exception e) {
            log.warn("Failed to get authorization token from request: {}", e.getMessage());
            return null;
        }
    }
}
