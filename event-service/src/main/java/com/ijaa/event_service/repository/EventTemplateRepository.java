package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventTemplateRepository extends JpaRepository<EventTemplate, Long> {
    
    // Find all active templates
    List<EventTemplate> findByIsActiveTrue();
    
    // Find public templates
    List<EventTemplate> findByIsPublicTrueAndIsActiveTrue();
    
    // Find templates by creator
    List<EventTemplate> findByCreatedByUsername(String username);
    
    // Find active templates by creator
    List<EventTemplate> findByCreatedByUsernameAndIsActiveTrue(String username);
    
    // Find templates by category
    List<EventTemplate> findByCategory(EventTemplate.TemplateCategory category);
    
    // Find active templates by category
    List<EventTemplate> findByCategoryAndIsActiveTrue(EventTemplate.TemplateCategory category);
    
    // Find public templates by category
    List<EventTemplate> findByCategoryAndIsPublicTrueAndIsActiveTrue(EventTemplate.TemplateCategory category);
    
    // Find templates that support recurrence
    List<EventTemplate> findBySupportsRecurrenceTrueAndIsActiveTrue();
    
    // Find templates by tags (search in tags field)
    @Query("SELECT et FROM EventTemplate et WHERE et.isActive = true AND (et.isPublic = true OR et.createdByUsername = ?1) AND et.tags LIKE %?2%")
    List<EventTemplate> findTemplatesByTag(String username, String tag);
    
    // Find templates by name (search in title field)
    @Query("SELECT et FROM EventTemplate et WHERE et.isActive = true AND (et.isPublic = true OR et.createdByUsername = ?1) AND et.title LIKE %?2%")
    List<EventTemplate> findTemplatesByName(String username, String name);
    
    // Find top rated templates
    @Query("SELECT et FROM EventTemplate et WHERE et.isActive = true AND (et.isPublic = true OR et.createdByUsername = ?1) ORDER BY et.averageRating DESC")
    List<EventTemplate> findTopRatedTemplates(String username);
    
    // Find most used templates
    @Query("SELECT et FROM EventTemplate et WHERE et.isActive = true AND (et.isPublic = true OR et.createdByUsername = ?1) ORDER BY et.usageCount DESC")
    List<EventTemplate> findMostUsedTemplates(String username);
    
    // Count templates by creator
    Long countByCreatedByUsername(String username);
    
    // Count active templates by creator
    Long countByCreatedByUsernameAndIsActiveTrue(String username);
    
    // Count public templates
    Long countByIsPublicTrueAndIsActiveTrue();
    
    // Find templates by event type
    List<EventTemplate> findByEventTypeAndIsActiveTrue(String eventType);
} 