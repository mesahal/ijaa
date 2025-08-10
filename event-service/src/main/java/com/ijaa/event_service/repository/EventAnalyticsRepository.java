package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventAnalyticsRepository extends JpaRepository<EventAnalytics, Long> {
    
    // Find analytics by event ID
    Optional<EventAnalytics> findByEventId(Long eventId);
    
    // Find analytics by organizer
    List<EventAnalytics> findByOrganizerUsername(String organizerUsername);
    
    // Find completed analytics
    List<EventAnalytics> findByIsCompletedTrue();
    
    // Find analytics by date range
    @Query("SELECT ea FROM EventAnalytics ea WHERE ea.eventStartDate >= ?1 AND ea.eventStartDate <= ?2")
    List<EventAnalytics> findAnalyticsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find analytics by organizer and date range
    @Query("SELECT ea FROM EventAnalytics ea WHERE ea.organizerUsername = ?1 AND ea.eventStartDate >= ?2 AND ea.eventStartDate <= ?3")
    List<EventAnalytics> findAnalyticsByOrganizerAndDateRange(String organizerUsername, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find top performing events by attendance rate
    @Query("SELECT ea FROM EventAnalytics ea WHERE ea.isCompleted = true ORDER BY ea.attendanceRate DESC")
    List<EventAnalytics> findTopPerformingEventsByAttendance();
    
    // Find top performing events by engagement rate
    @Query("SELECT ea FROM EventAnalytics ea WHERE ea.isCompleted = true ORDER BY ea.engagementRate DESC")
    List<EventAnalytics> findTopPerformingEventsByEngagement();
    
    // Count total events by organizer
    Long countByOrganizerUsername(String organizerUsername);
    
    // Count completed events by organizer
    Long countByOrganizerUsernameAndIsCompletedTrue(String organizerUsername);
    
    // Get average attendance rate by organizer
    @Query("SELECT AVG(ea.attendanceRate) FROM EventAnalytics ea WHERE ea.organizerUsername = ?1 AND ea.isCompleted = true")
    Double getAverageAttendanceRateByOrganizer(String organizerUsername);
    
    // Get average engagement rate by organizer
    @Query("SELECT AVG(ea.engagementRate) FROM EventAnalytics ea WHERE ea.organizerUsername = ?1 AND ea.isCompleted = true")
    Double getAverageEngagementRateByOrganizer(String organizerUsername);
} 