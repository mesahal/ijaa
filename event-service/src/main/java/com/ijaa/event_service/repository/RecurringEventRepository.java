package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.RecurringEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecurringEventRepository extends JpaRepository<RecurringEvent, Long> {
    
    // Find all active recurring events
    List<RecurringEvent> findByActiveTrue();
    
    // Find recurring events by creator
    List<RecurringEvent> findByCreatedByUsername(String username);
    
    // Find active recurring events by creator
    List<RecurringEvent> findByCreatedByUsernameAndActiveTrue(String username);
    
    // Find recurring events by type
    List<RecurringEvent> findByRecurrenceType(RecurringEvent.RecurrenceType recurrenceType);
    
    // Find recurring events ending after a specific date
    @Query("SELECT re FROM RecurringEvent re WHERE re.recurrenceEndDate >= ?1")
    List<RecurringEvent> findRecurringEventsEndingAfter(LocalDateTime date);
    
    // Find recurring events that should generate instances
    @Query("SELECT re FROM RecurringEvent re WHERE re.generateInstances = true AND re.active = true")
    List<RecurringEvent> findActiveRecurringEventsWithInstanceGeneration();
    
    // Find recurring events by organizer email
    List<RecurringEvent> findByOrganizerEmail(String organizerEmail);
    
    // Find active recurring events by organizer email
    List<RecurringEvent> findByOrganizerEmailAndActiveTrue(String organizerEmail);
    
    // Count active recurring events
    Long countByActiveTrue();
    
    // Count recurring events by creator
    Long countByCreatedByUsername(String username);
} 