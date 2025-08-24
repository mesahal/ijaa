package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.request.CalendarIntegrationRequest;
import com.ijaa.event_service.domain.response.CalendarIntegrationResponse;

import java.util.List;

public interface CalendarIntegrationService {
    
    // Get all integrations for a user
    List<CalendarIntegrationResponse> getIntegrationsByUser(String username);
    
    // Get integration by ID
    CalendarIntegrationResponse getIntegrationById(Long integrationId);
    
    // Create new integration
    CalendarIntegrationResponse createIntegration(CalendarIntegrationRequest request, String username);
    
    // Update existing integration
    CalendarIntegrationResponse updateIntegration(Long integrationId, CalendarIntegrationRequest request, String username);
    
    // Delete integration
    void deleteIntegration(Long integrationId, String username);
    
    // Activate integration
    CalendarIntegrationResponse activateIntegration(Long integrationId, String username);
    
    // Deactivate integration
    CalendarIntegrationResponse deactivateIntegration(Long integrationId, String username);
    
    // Get integration by username and calendar type
    CalendarIntegrationResponse getIntegrationByUserAndType(String username, String calendarType);
    
    // Sync events to external calendar
    void syncEventsToExternal(Long integrationId, String username);
    
    // Sync events from external calendar
    void syncEventsFromExternal(Long integrationId, String username);
    
    // Get integrations that need syncing
    List<CalendarIntegrationResponse> getIntegrationsNeedingSync();
    
    // Get integrations with errors
    List<CalendarIntegrationResponse> getIntegrationsWithErrors();
    
    // Test integration connection
    Boolean testIntegrationConnection(Long integrationId, String username);
    
    // Refresh access token
    CalendarIntegrationResponse refreshAccessToken(Long integrationId, String username);
    
    // Dashboard statistics
    Long getTotalIntegrationsByUser(String username);
    List<CalendarIntegrationResponse> getActiveIntegrationsByUser(String username);
    
    // Additional methods for API compatibility
    List<CalendarIntegrationResponse> getUserCalendarIntegrations(String username);
    List<CalendarIntegrationResponse> getActiveCalendarIntegrations(String username);
    CalendarIntegrationResponse getCalendarIntegrationById(Long integrationId);
    CalendarIntegrationResponse createCalendarIntegration(CalendarIntegrationRequest request, String username);
    CalendarIntegrationResponse updateCalendarIntegration(Long integrationId, CalendarIntegrationRequest request);
    void deleteCalendarIntegration(Long integrationId);
    CalendarIntegrationResponse activateCalendarIntegration(Long integrationId);
    CalendarIntegrationResponse deactivateCalendarIntegration(Long integrationId);
    CalendarIntegrationResponse syncEventsToCalendar(Long integrationId, List<Long> eventIds);
    CalendarIntegrationResponse syncInvitationsToCalendar(Long integrationId, List<Long> invitationIds);
    CalendarIntegrationResponse syncAllUserEvents(Long integrationId, String username);
    CalendarIntegrationResponse refreshTokens(Long integrationId);
    Boolean testCalendarConnection(Long integrationId);
    CalendarIntegrationResponse getSyncStatus(Long integrationId);
    List<CalendarIntegrationResponse> getFailedSyncs(String username);
    CalendarIntegrationResponse retryFailedSync(Long integrationId);
    List<String> getSupportedCalendarTypes();
    String getOAuthUrl(String calendarType, String redirectUri);
    CalendarIntegrationResponse handleOAuthCallback(String code, String state, String username);
} 