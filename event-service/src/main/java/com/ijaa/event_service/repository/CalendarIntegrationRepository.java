package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.CalendarIntegration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarIntegrationRepository extends JpaRepository<CalendarIntegration, Long> {
    
    // Find integrations by username
    List<CalendarIntegration> findByUsername(String username);
    
    // Find active integrations by username
    List<CalendarIntegration> findByUsernameAndIsActiveTrue(String username);
    
    // Find integration by username and calendar type
    Optional<CalendarIntegration> findByUsernameAndCalendarType(String username, CalendarIntegration.CalendarType calendarType);
    
    // Find integrations by calendar type
    List<CalendarIntegration> findByCalendarType(CalendarIntegration.CalendarType calendarType);
    
    // Find integrations that need syncing
    @Query("SELECT ci FROM CalendarIntegration ci WHERE ci.isActive = true AND (ci.lastSyncTime IS NULL OR ci.lastSyncTime < ?1)")
    List<CalendarIntegration> findIntegrationsNeedingSync(LocalDateTime syncThreshold);
    
    // Find integrations with sync errors
    @Query("SELECT ci FROM CalendarIntegration ci WHERE ci.lastSyncError IS NOT NULL AND ci.isActive = true")
    List<CalendarIntegration> findIntegrationsWithErrors();
    
    // Count active integrations by username
    Long countByUsernameAndIsActiveTrue(String username);
    
    // Count integrations by calendar type
    Long countByCalendarType(CalendarIntegration.CalendarType calendarType);
    
    // Find integrations that sync to external calendar
    List<CalendarIntegration> findBySyncToExternalTrueAndIsActiveTrue();
    
    // Find integrations that sync from external calendar
    List<CalendarIntegration> findBySyncFromExternalTrueAndIsActiveTrue();
} 