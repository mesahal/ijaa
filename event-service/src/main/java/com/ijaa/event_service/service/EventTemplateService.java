package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.request.EventTemplateRequest;
import com.ijaa.event_service.domain.response.EventTemplateResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventTemplateService {
    
    // Get all active templates
    List<EventTemplateResponse> getAllActiveTemplates();
    
    // Get public templates
    List<EventTemplateResponse> getPublicTemplates();
    
    // Get template by ID
    EventTemplateResponse getTemplateById(Long templateId);
    
    // Create new template
    EventTemplateResponse createTemplate(EventTemplateRequest request, String username);
    
    // Update existing template
    EventTemplateResponse updateTemplate(Long templateId, EventTemplateRequest request, String username);
    
    // Delete template
    void deleteTemplate(Long templateId, String username);
    
    // Activate template
    EventTemplateResponse activateTemplate(Long templateId, String username);
    
    // Deactivate template
    EventTemplateResponse deactivateTemplate(Long templateId, String username);
    
    // User-specific methods
    List<EventTemplateResponse> getTemplatesByUser(String username);
    
    EventTemplateResponse createTemplateForUser(EventTemplateRequest request, String username);
    
    EventTemplateResponse updateTemplateForUser(Long templateId, EventTemplateRequest request, String username);
    
    void deleteTemplateForUser(Long templateId, String username);
    
    EventTemplateResponse getTemplateByIdForUser(Long templateId, String username);
    
    // Search templates
    List<EventTemplateResponse> searchTemplates(String name, String title, String description, String location, String eventType, String organizerName, String username);
    
    // Get templates by category
    List<EventTemplateResponse> getTemplatesByCategory(String category, String username);
    
    // Get templates that support recurrence
    List<EventTemplateResponse> getTemplatesSupportingRecurrence(String username);
    
    // Get top rated templates
    List<EventTemplateResponse> getTopRatedTemplates(String username);
    
    // Get most used templates
    List<EventTemplateResponse> getMostUsedTemplates(String username);
    
    // Create event from template
    EventTemplateResponse createEventFromTemplate(Long templateId, LocalDateTime startDate, String username);
    
    // Rate template
    EventTemplateResponse rateTemplate(Long templateId, Double rating, String username);
    
    // Increment usage count
    EventTemplateResponse incrementUsageCount(Long templateId);
    
    // Dashboard statistics
    Long getTotalTemplatesByUser(String username);
    Long getActiveTemplatesByUser(String username);
    Long getPublicTemplatesCount();
    
    // Additional methods for API compatibility
    List<EventTemplateResponse> getAllTemplates();
    List<EventTemplateResponse> getAvailableTemplates(String username);
    EventTemplateResponse makeTemplatePublic(Long templateId);
    EventTemplateResponse makeTemplatePrivate(Long templateId);
    List<EventTemplateResponse> getMostUsedTemplatesByUser(String username);
    List<EventTemplateResponse> getTemplatesByEventType(String eventType);
    List<EventTemplateResponse> getRecurringTemplates();
    List<EventTemplateResponse> getRecurringTemplatesByUser(String username);
    EventTemplateResponse duplicateTemplate(Long templateId, String newName, String username);
    String exportTemplate(Long templateId, String format);
    EventTemplateResponse importTemplate(String templateData, String format, String username);
} 