package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.EventReminderRequest;
import com.ijaa.event_service.domain.response.EventReminderResponse;

import java.util.List;

public interface EventReminderService {

    // Set a reminder for an event
    EventReminderResponse setReminder(EventReminderRequest request, String username);

    // Get reminders for an event
    List<EventReminderResponse> getEventReminders(Long eventId);

    // Get user's reminders
    List<EventReminderResponse> getUserReminders(String username);

    // Get a specific reminder
    EventReminderResponse getReminder(Long reminderId);

    // Update reminder
    EventReminderResponse updateReminder(Long reminderId, EventReminderRequest request, String username);

    // Delete reminder
    void deleteReminder(Long reminderId, String username);

    // Get sent reminders for a user
    PagedResponse<EventReminderResponse> getSentReminders(String username, int page, int size);

    // Process reminders that need to be sent (for scheduled job)
    List<EventReminderResponse> processRemindersToSend();

    // Mark reminder as sent
    EventReminderResponse markReminderAsSent(Long reminderId);

    // Get reminders by type
    List<EventReminderResponse> getRemindersByType(String reminderType);

    // Get reminders by channel
    List<EventReminderResponse> getRemindersByChannel(String channel);
} 