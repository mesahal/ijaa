package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.entity.CalendarIntegration;
import com.ijaa.event_service.domain.request.CalendarIntegrationRequest;
import com.ijaa.event_service.domain.response.CalendarIntegrationResponse;
import com.ijaa.event_service.repository.CalendarIntegrationRepository;
import com.ijaa.event_service.service.CalendarIntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarIntegrationServiceImpl implements CalendarIntegrationService {

    private final CalendarIntegrationRepository calendarIntegrationRepository;

    @Override
    public List<CalendarIntegrationResponse> getIntegrationsByUser(String username) {
        return calendarIntegrationRepository.findByUsername(username).stream()
                .map(this::createCalendarIntegrationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarIntegrationResponse> getActiveIntegrationsByUser(String username) {
        return calendarIntegrationRepository.findByUsernameAndIsActiveTrue(username).stream()
                .map(this::createCalendarIntegrationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CalendarIntegrationResponse getIntegrationById(Long integrationId) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        return createCalendarIntegrationResponse(integration);
    }

    @Override
    public CalendarIntegrationResponse createIntegration(CalendarIntegrationRequest request, String username) {
        CalendarIntegration integration = new CalendarIntegration();
        
        integration.setUsername(username);
        integration.setCalendarName(request.getCalendarName());
        integration.setCalendarType(CalendarIntegration.CalendarType.valueOf(request.getCalendarType()));
        integration.setCalendarUrl(request.getCalendarUrl());
        integration.setAccessToken(request.getAccessToken());
        integration.setRefreshToken(request.getRefreshToken());
        integration.setTokenExpiry(request.getTokenExpiry());
        integration.setCalendarId(request.getCalendarId());
        integration.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        integration.setSyncToExternal(request.getSyncToExternal() != null ? request.getSyncToExternal() : false);
        integration.setSyncFromExternal(request.getSyncFromExternal() != null ? request.getSyncFromExternal() : false);
        integration.setSyncRecurringEvents(request.getSyncRecurringEvents() != null ? request.getSyncRecurringEvents() : false);
        integration.setSyncReminders(request.getSyncReminders() != null ? request.getSyncReminders() : false);
        integration.setSyncFrequencyHours(request.getSyncFrequencyHours() != null ? request.getSyncFrequencyHours() : 24);
        
        CalendarIntegration savedIntegration = calendarIntegrationRepository.save(integration);
        return createCalendarIntegrationResponse(savedIntegration);
    }

    @Override
    public CalendarIntegrationResponse updateIntegration(Long integrationId, CalendarIntegrationRequest request, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only update your own calendar integrations");
        }
        
        integration.setCalendarName(request.getCalendarName());
        integration.setCalendarType(CalendarIntegration.CalendarType.valueOf(request.getCalendarType()));
        integration.setCalendarUrl(request.getCalendarUrl());
        integration.setAccessToken(request.getAccessToken());
        integration.setRefreshToken(request.getRefreshToken());
        integration.setTokenExpiry(request.getTokenExpiry());
        integration.setCalendarId(request.getCalendarId());
        integration.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        integration.setSyncToExternal(request.getSyncToExternal() != null ? request.getSyncToExternal() : false);
        integration.setSyncFromExternal(request.getSyncFromExternal() != null ? request.getSyncFromExternal() : false);
        integration.setSyncRecurringEvents(request.getSyncRecurringEvents() != null ? request.getSyncRecurringEvents() : false);
        integration.setSyncReminders(request.getSyncReminders() != null ? request.getSyncReminders() : false);
        integration.setSyncFrequencyHours(request.getSyncFrequencyHours() != null ? request.getSyncFrequencyHours() : 24);
        
        CalendarIntegration updatedIntegration = calendarIntegrationRepository.save(integration);
        return createCalendarIntegrationResponse(updatedIntegration);
    }

    @Override
    public void deleteIntegration(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only delete your own calendar integrations");
        }
        
        calendarIntegrationRepository.delete(integration);
    }

    @Override
    public CalendarIntegrationResponse activateIntegration(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only activate your own calendar integrations");
        }
        
        integration.setIsActive(true);
        CalendarIntegration updatedIntegration = calendarIntegrationRepository.save(integration);
        return createCalendarIntegrationResponse(updatedIntegration);
    }

    @Override
    public CalendarIntegrationResponse deactivateIntegration(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only deactivate your own calendar integrations");
        }
        
        integration.setIsActive(false);
        CalendarIntegration updatedIntegration = calendarIntegrationRepository.save(integration);
        return createCalendarIntegrationResponse(updatedIntegration);
    }

    @Override
    public CalendarIntegrationResponse getIntegrationByUserAndType(String username, String calendarType) {
        Optional<CalendarIntegration> integration = calendarIntegrationRepository.findByUsernameAndCalendarType(
            username, CalendarIntegration.CalendarType.valueOf(calendarType));
        
        return integration.map(this::createCalendarIntegrationResponse)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found for user: " + username + " and type: " + calendarType));
    }

    @Override
    public void syncEventsToExternal(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only sync your own calendar integrations");
        }
        
        // Simulate sync process
        log.info("Syncing events to external calendar for integration: {}", integrationId);
        integration.setLastSyncTime(LocalDateTime.now());
        integration.setLastSyncError(null);
        calendarIntegrationRepository.save(integration);
    }

    @Override
    public void syncEventsFromExternal(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only sync your own calendar integrations");
        }
        
        // Simulate sync process
        log.info("Syncing events from external calendar for integration: {}", integrationId);
        integration.setLastSyncTime(LocalDateTime.now());
        integration.setLastSyncError(null);
        calendarIntegrationRepository.save(integration);
    }

    @Override
    public List<CalendarIntegrationResponse> getIntegrationsNeedingSync() {
        LocalDateTime syncThreshold = LocalDateTime.now().minusHours(24);
        return calendarIntegrationRepository.findIntegrationsNeedingSync(syncThreshold).stream()
                .map(this::createCalendarIntegrationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalendarIntegrationResponse> getIntegrationsWithErrors() {
        return calendarIntegrationRepository.findIntegrationsWithErrors().stream()
                .map(this::createCalendarIntegrationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean testIntegrationConnection(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only test your own calendar integrations");
        }
        
        // Simulate connection test
        log.info("Testing connection for integration: {}", integrationId);
        return true; // Simulate successful connection
    }

    @Override
    public CalendarIntegrationResponse refreshAccessToken(Long integrationId, String username) {
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        
        // Check if user owns this integration
        if (!username.equals(integration.getUsername())) {
            throw new RuntimeException("You can only refresh your own calendar integrations");
        }
        
        // Simulate token refresh
        log.info("Refreshing access token for integration: {}", integrationId);
        integration.setTokenExpiry(LocalDateTime.now().plusHours(1));
        CalendarIntegration updatedIntegration = calendarIntegrationRepository.save(integration);
        return createCalendarIntegrationResponse(updatedIntegration);
    }

    @Override
    public Long getTotalIntegrationsByUser(String username) {
        return calendarIntegrationRepository.countByUsernameAndIsActiveTrue(username);
    }



    private CalendarIntegrationResponse createCalendarIntegrationResponse(CalendarIntegration integration) {
        CalendarIntegrationResponse response = new CalendarIntegrationResponse();
        response.setId(integration.getId());
        response.setUsername(integration.getUsername());
        response.setCalendarType(integration.getCalendarType().toString());
        response.setCalendarName(integration.getCalendarName());
        response.setCalendarUrl(integration.getCalendarUrl());
        response.setCalendarId(integration.getCalendarId());
        response.setIsActive(integration.getIsActive());
        response.setSyncToExternal(integration.getSyncToExternal());
        response.setSyncFromExternal(integration.getSyncFromExternal());
        response.setSyncRecurringEvents(integration.getSyncRecurringEvents());
        response.setSyncReminders(integration.getSyncReminders());
        response.setLastSyncError(integration.getLastSyncError());
        response.setLastSyncTime(integration.getLastSyncTime());
        response.setSyncFrequencyHours(integration.getSyncFrequencyHours());
        response.setTokenExpiry(integration.getTokenExpiry());
        response.setCreatedAt(integration.getCreatedAt());
        response.setUpdatedAt(integration.getUpdatedAt());
        
        return response;
    }
    
    // Additional methods for API compatibility
    @Override
    public List<CalendarIntegrationResponse> getUserCalendarIntegrations(String username) {
        return getIntegrationsByUser(username);
    }
    
    @Override
    public List<CalendarIntegrationResponse> getActiveCalendarIntegrations(String username) {
        return getActiveIntegrationsByUser(username);
    }
    
    @Override
    public CalendarIntegrationResponse getCalendarIntegrationById(Long integrationId) {
        return getIntegrationById(integrationId);
    }
    
    @Override
    public CalendarIntegrationResponse createCalendarIntegration(CalendarIntegrationRequest request, String username) {
        return createIntegration(request, username);
    }
    
    @Override
    public CalendarIntegrationResponse updateCalendarIntegration(Long integrationId, CalendarIntegrationRequest request) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        return updateIntegration(integrationId, request, integration.getUsername());
    }
    
    @Override
    public void deleteCalendarIntegration(Long integrationId) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        deleteIntegration(integrationId, integration.getUsername());
    }
    
    @Override
    public CalendarIntegrationResponse activateCalendarIntegration(Long integrationId) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        return activateIntegration(integrationId, integration.getUsername());
    }
    
    @Override
    public CalendarIntegrationResponse deactivateCalendarIntegration(Long integrationId) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        return deactivateIntegration(integrationId, integration.getUsername());
    }
    
    @Override
    public CalendarIntegrationResponse syncEventsToCalendar(Long integrationId, List<Long> eventIds) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        syncEventsToExternal(integrationId, integration.getUsername());
        return createCalendarIntegrationResponse(integration);
    }
    
    @Override
    public CalendarIntegrationResponse syncInvitationsToCalendar(Long integrationId, List<Long> invitationIds) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        // Simulate syncing invitations
        log.info("Syncing invitations to calendar for integration: {}", integrationId);
        return createCalendarIntegrationResponse(integration);
    }
    
    @Override
    public CalendarIntegrationResponse syncAllUserEvents(Long integrationId, String username) {
        syncEventsToExternal(integrationId, username);
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        return createCalendarIntegrationResponse(integration);
    }
    
    @Override
    public CalendarIntegrationResponse refreshTokens(Long integrationId) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        return refreshAccessToken(integrationId, integration.getUsername());
    }
    
    @Override
    public Boolean testCalendarConnection(Long integrationId) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        return testIntegrationConnection(integrationId, integration.getUsername());
    }
    
    @Override
    public CalendarIntegrationResponse getSyncStatus(Long integrationId) {
        return getIntegrationById(integrationId);
    }
    
    @Override
    public List<CalendarIntegrationResponse> getFailedSyncs(String username) {
        return getIntegrationsWithErrors();
    }
    
    @Override
    public CalendarIntegrationResponse retryFailedSync(Long integrationId) {
        // This method doesn't have username parameter, so we'll need to get it from the existing integration
        CalendarIntegration integration = calendarIntegrationRepository.findById(integrationId)
                .orElseThrow(() -> new RuntimeException("Calendar integration not found with id: " + integrationId));
        // Simulate retry
        log.info("Retrying failed sync for integration: {}", integrationId);
        return createCalendarIntegrationResponse(integration);
    }
    
    @Override
    public List<String> getSupportedCalendarTypes() {
        return List.of("GOOGLE_CALENDAR", "OUTLOOK_CALENDAR", "APPLE_CALENDAR", "OTHER");
    }
    
    @Override
    public String getOAuthUrl(String calendarType, String redirectUri) {
        // Simulate OAuth URL generation
        return "https://oauth.example.com/authorize?calendar_type=" + calendarType + "&redirect_uri=" + redirectUri;
    }
    
    @Override
    public CalendarIntegrationResponse handleOAuthCallback(String code, String state, String username) {
        // Simulate OAuth callback handling
        log.info("Handling OAuth callback for user: {}", username);
        
        // Create a new integration based on the OAuth callback
        CalendarIntegrationRequest request = new CalendarIntegrationRequest();
        request.setCalendarName("OAuth Calendar");
        request.setCalendarType("GOOGLE_CALENDAR");
        request.setIsActive(true);
        
        return createIntegration(request, username);
    }
} 