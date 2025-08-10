package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventReminder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventReminderRepository extends JpaRepository<EventReminder, Long> {

    // Find reminders for an event
    List<EventReminder> findByEventIdAndIsActiveTrueOrderByReminderTimeAsc(Long eventId);

    // Find reminders for a user
    List<EventReminder> findByUsernameAndIsActiveTrueOrderByReminderTimeAsc(String username);

    // Find reminders for an event by a specific user
    EventReminder findByEventIdAndUsernameAndIsActiveTrue(Long eventId, String username);

    // Find reminders that need to be sent (for scheduled job)
    @Query("SELECT r FROM EventReminder r WHERE r.reminderTime <= :now AND r.isSent = false AND r.isActive = true")
    List<EventReminder> findRemindersToSend(@Param("now") LocalDateTime now);

    // Find sent reminders for a user
    Page<EventReminder> findByUsernameAndIsSentTrueOrderByReminderTimeDesc(
            String username, Pageable pageable);

    // Count active reminders for an event
    @Query("SELECT COUNT(r) FROM EventReminder r WHERE r.eventId = :eventId AND r.isActive = true")
    Long countActiveByEventId(@Param("eventId") Long eventId);

    // Find reminders by type
    List<EventReminder> findByReminderTypeAndIsActiveTrueOrderByReminderTimeAsc(
            EventReminder.ReminderType reminderType);

    // Find reminders by channel
    List<EventReminder> findByChannelAndIsActiveTrueOrderByReminderTimeAsc(
            EventReminder.NotificationChannel channel);
} 