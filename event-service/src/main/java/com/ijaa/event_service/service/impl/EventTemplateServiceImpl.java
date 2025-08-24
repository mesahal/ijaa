package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.entity.EventTemplate;
import com.ijaa.event_service.domain.request.EventTemplateRequest;
import com.ijaa.event_service.domain.response.EventTemplateResponse;
import com.ijaa.event_service.repository.EventTemplateRepository;
import com.ijaa.event_service.service.EventTemplateService;
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
public class EventTemplateServiceImpl implements EventTemplateService {

    private final EventTemplateRepository eventTemplateRepository;

    @Override
    public List<EventTemplateResponse> getAllActiveTemplates() {
        return eventTemplateRepository.findByIsActiveTrue().stream()
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventTemplateResponse> getPublicTemplates() {
        return eventTemplateRepository.findByIsPublicTrueAndIsActiveTrue().stream()
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventTemplateResponse getTemplateById(Long templateId) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        return createEventTemplateResponse(template);
    }

    @Override
    public EventTemplateResponse createTemplate(EventTemplateRequest request, String username) {
        EventTemplate template = new EventTemplate();
        template.setName(request.getName());
        template.setCreatedByUsername(username);
        template.setCategory(EventTemplate.TemplateCategory.valueOf(request.getCategory()));
        template.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : false);
        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());
        template.setLocation(request.getLocation());
        template.setEventType(request.getEventType());
        template.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        template.setMeetingLink(request.getMeetingLink());
        template.setMaxParticipants(request.getMaxParticipants());
        template.setOrganizerName(request.getOrganizerName());
        template.setOrganizerEmail(request.getOrganizerEmail());
        template.setInviteMessage(request.getInviteMessage());
        template.setPrivacy(request.getPrivacy() != null ? EventTemplate.EventPrivacy.valueOf(request.getPrivacy()) : EventTemplate.EventPrivacy.PUBLIC);
        template.setDefaultDurationMinutes(request.getDefaultDurationMinutes() != null ? request.getDefaultDurationMinutes() : 60);
        template.setDefaultStartTime(request.getDefaultStartTime());
        template.setDefaultEndTime(request.getDefaultEndTime());
        template.setSupportsRecurrence(request.getSupportsRecurrence() != null ? request.getSupportsRecurrence() : false);
        template.setDefaultRecurrenceType(request.getDefaultRecurrenceType() != null ? EventTemplate.RecurrenceType.valueOf(request.getDefaultRecurrenceType()) : null);
        template.setDefaultRecurrenceInterval(request.getDefaultRecurrenceInterval() != null ? request.getDefaultRecurrenceInterval() : 1);
        template.setDefaultRecurrenceDays(request.getDefaultRecurrenceDays());
        template.setTags(request.getTags());
        template.setUsageCount(0);
        template.setAverageRating(0.0);
        template.setTotalRatings(0);
        
        EventTemplate savedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(savedTemplate);
    }

    @Override
    public EventTemplateResponse updateTemplate(Long templateId, EventTemplateRequest request, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user owns this template
        if (!username.equals(template.getCreatedByUsername())) {
            throw new RuntimeException("You can only update your own event templates");
        }
        
        template.setName(request.getName());
        template.setCategory(EventTemplate.TemplateCategory.valueOf(request.getCategory()));
        template.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : false);
        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());
        template.setLocation(request.getLocation());
        template.setEventType(request.getEventType());
        template.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        template.setMeetingLink(request.getMeetingLink());
        template.setMaxParticipants(request.getMaxParticipants());
        template.setOrganizerName(request.getOrganizerName());
        template.setOrganizerEmail(request.getOrganizerEmail());
        template.setInviteMessage(request.getInviteMessage());
        template.setPrivacy(request.getPrivacy() != null ? EventTemplate.EventPrivacy.valueOf(request.getPrivacy()) : EventTemplate.EventPrivacy.PUBLIC);
        template.setDefaultDurationMinutes(request.getDefaultDurationMinutes() != null ? request.getDefaultDurationMinutes() : 60);
        template.setDefaultStartTime(request.getDefaultStartTime());
        template.setDefaultEndTime(request.getDefaultEndTime());
        template.setSupportsRecurrence(request.getSupportsRecurrence() != null ? request.getSupportsRecurrence() : false);
        template.setDefaultRecurrenceType(request.getDefaultRecurrenceType() != null ? EventTemplate.RecurrenceType.valueOf(request.getDefaultRecurrenceType()) : null);
        template.setDefaultRecurrenceInterval(request.getDefaultRecurrenceInterval() != null ? request.getDefaultRecurrenceInterval() : 1);
        template.setDefaultRecurrenceDays(request.getDefaultRecurrenceDays());
        template.setTags(request.getTags());
        
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }

    @Override
    public void deleteTemplate(Long templateId, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user owns this template
        if (!username.equals(template.getCreatedByUsername())) {
            throw new RuntimeException("You can only delete your own event templates");
        }
        
        eventTemplateRepository.delete(template);
    }

    @Override
    public void deleteTemplateForUser(Long templateId, String username) {
        deleteTemplate(templateId, username);
    }

    @Override
    public EventTemplateResponse activateTemplate(Long templateId, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user owns this template
        if (!username.equals(template.getCreatedByUsername())) {
            throw new RuntimeException("You can only activate your own event templates");
        }
        
        template.setIsActive(true);
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }

    @Override
    public EventTemplateResponse deactivateTemplate(Long templateId, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user owns this template
        if (!username.equals(template.getCreatedByUsername())) {
            throw new RuntimeException("You can only deactivate your own event templates");
        }
        
        template.setIsActive(false);
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }

    @Override
    public List<EventTemplateResponse> getTemplatesByUser(String username) {
        return eventTemplateRepository.findByCreatedByUsername(username).stream()
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventTemplateResponse createTemplateForUser(EventTemplateRequest request, String username) {
        return createTemplate(request, username);
    }

    @Override
    public EventTemplateResponse updateTemplateForUser(Long templateId, EventTemplateRequest request, String username) {
        return updateTemplate(templateId, request, username);
    }

    @Override
    public EventTemplateResponse getTemplateByIdForUser(Long templateId, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user owns this template or if it's public
        if (!username.equals(template.getCreatedByUsername()) && !template.getIsPublic()) {
            throw new RuntimeException("You can only access your own event templates or public templates");
        }
        
        return createEventTemplateResponse(template);
    }

    @Override
    public List<EventTemplateResponse> searchTemplates(String name, String title, String description, String location, String eventType, String organizerName, String username) {
        // Enhanced search implementation
        return eventTemplateRepository.findByIsActiveTrue().stream()
                .filter(template -> name == null || template.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(template -> title == null || (template.getTitle() != null && template.getTitle().toLowerCase().contains(title.toLowerCase())))
                .filter(template -> description == null || (template.getDescription() != null && template.getDescription().toLowerCase().contains(description.toLowerCase())))
                .filter(template -> location == null || (template.getLocation() != null && template.getLocation().toLowerCase().contains(location.toLowerCase())))
                .filter(template -> eventType == null || (template.getEventType() != null && template.getEventType().equalsIgnoreCase(eventType)))
                .filter(template -> organizerName == null || (template.getOrganizerName() != null && template.getOrganizerName().toLowerCase().contains(organizerName.toLowerCase())))
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventTemplateResponse> getTemplatesByCategory(String category, String username) {
        return eventTemplateRepository.findByCategoryAndIsActiveTrue(EventTemplate.TemplateCategory.valueOf(category)).stream()
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventTemplateResponse> getTemplatesSupportingRecurrence(String username) {
        return eventTemplateRepository.findBySupportsRecurrenceTrueAndIsActiveTrue().stream()
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventTemplateResponse> getTopRatedTemplates(String username) {
        return eventTemplateRepository.findTopRatedTemplates(username).stream()
                .limit(10)
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventTemplateResponse> getMostUsedTemplates(String username) {
        return eventTemplateRepository.findMostUsedTemplates(username).stream()
                .limit(10)
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventTemplateResponse createEventFromTemplate(Long templateId, LocalDateTime startDate, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user can access this template
        if (!username.equals(template.getCreatedByUsername()) && !template.getIsPublic()) {
            throw new RuntimeException("You can only use your own event templates or public templates");
        }
        
        // Increment usage count
        template.setUsageCount(template.getUsageCount() + 1);
        eventTemplateRepository.save(template);
        
        return createEventTemplateResponse(template);
    }

    @Override
    public EventTemplateResponse rateTemplate(Long templateId, Double rating, String username) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Check if user can rate this template
        if (!username.equals(template.getCreatedByUsername()) && !template.getIsPublic()) {
            throw new RuntimeException("You can only rate your own event templates or public templates");
        }
        
        // Update rating
        int totalRatings = template.getTotalRatings() + 1;
        double newAverageRating = ((template.getAverageRating() * template.getTotalRatings()) + rating) / totalRatings;
        
        template.setAverageRating(newAverageRating);
        template.setTotalRatings(totalRatings);
        
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }

    @Override
    public EventTemplateResponse incrementUsageCount(Long templateId) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        template.setUsageCount(template.getUsageCount() + 1);
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }

    @Override
    public Long getTotalTemplatesByUser(String username) {
        return eventTemplateRepository.countByCreatedByUsername(username);
    }

    @Override
    public Long getActiveTemplatesByUser(String username) {
        return eventTemplateRepository.countByCreatedByUsernameAndIsActiveTrue(username);
    }

    @Override
    public Long getPublicTemplatesCount() {
        return eventTemplateRepository.countByIsPublicTrueAndIsActiveTrue();
    }

    private EventTemplateResponse createEventTemplateResponse(EventTemplate template) {
        EventTemplateResponse response = new EventTemplateResponse();
        response.setId(template.getId());
        response.setName(template.getName());
        response.setCreatedByUsername(template.getCreatedByUsername());
        response.setCategory(template.getCategory().toString());
        response.setIsPublic(template.getIsPublic());
        response.setIsActive(template.getIsActive());
        response.setTitle(template.getTitle());
        response.setDescription(template.getDescription());
        response.setLocation(template.getLocation());
        response.setEventType(template.getEventType());
        response.setIsOnline(template.getIsOnline());
        response.setMeetingLink(template.getMeetingLink());
        response.setMaxParticipants(template.getMaxParticipants());
        response.setOrganizerName(template.getOrganizerName());
        response.setOrganizerEmail(template.getOrganizerEmail());
        response.setInviteMessage(template.getInviteMessage());
        response.setPrivacy(template.getPrivacy() != null ? template.getPrivacy().toString() : null);
        response.setDefaultDurationMinutes(template.getDefaultDurationMinutes());
        response.setDefaultStartTime(template.getDefaultStartTime());
        response.setDefaultEndTime(template.getDefaultEndTime());
        response.setSupportsRecurrence(template.getSupportsRecurrence());
        response.setDefaultRecurrenceType(template.getDefaultRecurrenceType() != null ? template.getDefaultRecurrenceType().toString() : null);
        response.setDefaultRecurrenceInterval(template.getDefaultRecurrenceInterval());
        response.setDefaultRecurrenceDays(template.getDefaultRecurrenceDays());
        response.setUsageCount(template.getUsageCount());
        response.setAverageRating(template.getAverageRating());
        response.setTotalRatings(template.getTotalRatings());
        response.setTags(template.getTags());
        response.setCreatedAt(template.getCreatedAt());
        response.setUpdatedAt(template.getUpdatedAt());
        
        return response;
    }
    
    // Additional methods for API compatibility
    @Override
    public List<EventTemplateResponse> getAllTemplates() {
        return getAllActiveTemplates();
    }
    
    @Override
    public List<EventTemplateResponse> getAvailableTemplates(String username) {
        return getTemplatesByUser(username);
    }
    
    @Override
    public EventTemplateResponse makeTemplatePublic(Long templateId) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        template.setIsPublic(true);
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }
    
    @Override
    public EventTemplateResponse makeTemplatePrivate(Long templateId) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        template.setIsPublic(false);
        EventTemplate updatedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(updatedTemplate);
    }
    
    @Override
    public List<EventTemplateResponse> getMostUsedTemplatesByUser(String username) {
        return getMostUsedTemplates(username);
    }
    
    @Override
    public List<EventTemplateResponse> getTemplatesByEventType(String eventType) {
        return eventTemplateRepository.findByEventTypeAndIsActiveTrue(eventType).stream()
                .map(this::createEventTemplateResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<EventTemplateResponse> getRecurringTemplates() {
        return getTemplatesSupportingRecurrence(null);
    }
    
    @Override
    public List<EventTemplateResponse> getRecurringTemplatesByUser(String username) {
        return getTemplatesSupportingRecurrence(username);
    }
    
    @Override
    public EventTemplateResponse duplicateTemplate(Long templateId, String newName, String username) {
        EventTemplate originalTemplate = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        EventTemplate newTemplate = new EventTemplate();
        newTemplate.setName(newName);
        newTemplate.setCreatedByUsername(username);
        newTemplate.setCategory(originalTemplate.getCategory());
        newTemplate.setIsPublic(false);
        newTemplate.setTitle(originalTemplate.getTitle());
        newTemplate.setDescription(originalTemplate.getDescription());
        newTemplate.setLocation(originalTemplate.getLocation());
        newTemplate.setEventType(originalTemplate.getEventType());
        newTemplate.setIsOnline(originalTemplate.getIsOnline());
        newTemplate.setMeetingLink(originalTemplate.getMeetingLink());
        newTemplate.setMaxParticipants(originalTemplate.getMaxParticipants());
        newTemplate.setOrganizerName(originalTemplate.getOrganizerName());
        newTemplate.setOrganizerEmail(originalTemplate.getOrganizerEmail());
        newTemplate.setInviteMessage(originalTemplate.getInviteMessage());
        newTemplate.setPrivacy(originalTemplate.getPrivacy());
        newTemplate.setDefaultDurationMinutes(originalTemplate.getDefaultDurationMinutes());
        newTemplate.setDefaultStartTime(originalTemplate.getDefaultStartTime());
        newTemplate.setDefaultEndTime(originalTemplate.getDefaultEndTime());
        newTemplate.setSupportsRecurrence(originalTemplate.getSupportsRecurrence());
        newTemplate.setDefaultRecurrenceType(originalTemplate.getDefaultRecurrenceType());
        newTemplate.setDefaultRecurrenceInterval(originalTemplate.getDefaultRecurrenceInterval());
        newTemplate.setDefaultRecurrenceDays(originalTemplate.getDefaultRecurrenceDays());
        newTemplate.setTags(originalTemplate.getTags());
        newTemplate.setUsageCount(0);
        newTemplate.setAverageRating(0.0);
        newTemplate.setTotalRatings(0);
        
        EventTemplate savedTemplate = eventTemplateRepository.save(newTemplate);
        return createEventTemplateResponse(savedTemplate);
    }
    
    @Override
    public String exportTemplate(Long templateId, String format) {
        EventTemplate template = eventTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Event template not found with id: " + templateId));
        
        // Simulate export functionality
        return "Template exported in " + format + " format: " + template.getName();
    }
    
    @Override
    public EventTemplateResponse importTemplate(String templateData, String format, String username) {
        // Simulate import functionality
        EventTemplate template = new EventTemplate();
        template.setName("Imported Template");
        template.setCreatedByUsername(username);
        template.setCategory(EventTemplate.TemplateCategory.OTHER);
        template.setIsPublic(false);
        template.setTitle("Imported Event");
        template.setDescription("Template imported from " + format + " format");
        template.setMaxParticipants(50);
        template.setUsageCount(0);
        template.setAverageRating(0.0);
        template.setTotalRatings(0);
        
        EventTemplate savedTemplate = eventTemplateRepository.save(template);
        return createEventTemplateResponse(savedTemplate);
    }
} 