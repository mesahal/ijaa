package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.entity.EventReminder;
import com.ijaa.event_service.domain.request.EventReminderRequest;
import com.ijaa.event_service.domain.response.EventReminderResponse;
import com.ijaa.event_service.repository.EventReminderRepository;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.service.EventReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventReminderServiceImpl implements EventReminderService {

    private final EventReminderRepository eventReminderRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventReminderResponse setReminder(EventReminderRequest request, String username) {
        log.info("Setting reminder for event: {} by user: {}", request.getEventId(), username);

        // Verify event exists
        if (!eventRepository.existsById(request.getEventId())) {
            throw new RuntimeException("Event not found");
        }

        // Check if user already has a reminder for this event
        EventReminder existingReminder = eventReminderRepository
                .findByEventIdAndUsernameAndIsActiveTrue(request.getEventId(), username);
        if (existingReminder != null) {
            throw new RuntimeException("User already has a reminder for this event");
        }

        EventReminder reminder = new EventReminder();
        reminder.setEventId(request.getEventId());
        reminder.setUsername(username);
        reminder.setReminderTime(request.getReminderTime());
        reminder.setReminderType(EventReminder.ReminderType.valueOf(request.getReminderType()));
        reminder.setCustomMessage(request.getCustomMessage());
        reminder.setChannel(EventReminder.NotificationChannel.valueOf(request.getChannel()));

        EventReminder savedReminder = eventReminderRepository.save(reminder);
        return mapToResponse(savedReminder);
    }

    @Override
    public List<EventReminderResponse> getEventReminders(Long eventId) {
        log.info("Getting reminders for event: {}", eventId);

        List<EventReminder> reminders = eventReminderRepository.findByEventIdAndIsActiveTrueOrderByReminderTimeAsc(eventId);
        return reminders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventReminderResponse> getUserReminders(String username) {
        log.info("Getting reminders for user: {}", username);

        List<EventReminder> reminders = eventReminderRepository.findByUsernameAndIsActiveTrueOrderByReminderTimeAsc(username);
        return reminders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventReminderResponse getReminder(Long reminderId) {
        log.info("Getting reminder: {}", reminderId);

        EventReminder reminder = eventReminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        return mapToResponse(reminder);
    }

    @Override
    @Transactional
    public EventReminderResponse updateReminder(Long reminderId, EventReminderRequest request, String username) {
        log.info("Updating reminder: {} by user: {}", reminderId, username);

        EventReminder reminder = eventReminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (!reminder.getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to update this reminder");
        }

        reminder.setReminderTime(request.getReminderTime());
        reminder.setReminderType(EventReminder.ReminderType.valueOf(request.getReminderType()));
        reminder.setCustomMessage(request.getCustomMessage());
        reminder.setChannel(EventReminder.NotificationChannel.valueOf(request.getChannel()));

        EventReminder updatedReminder = eventReminderRepository.save(reminder);
        return mapToResponse(updatedReminder);
    }

    @Override
    @Transactional
    public void deleteReminder(Long reminderId, String username) {
        log.info("Deleting reminder: {} by user: {}", reminderId, username);

        EventReminder reminder = eventReminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        if (!reminder.getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this reminder");
        }

        reminder.setIsActive(false);
        eventReminderRepository.save(reminder);
    }

    @Override
    public PagedResponse<EventReminderResponse> getSentReminders(String username, int page, int size) {
        log.info("Getting sent reminders for user: {}, page: {}, size: {}", username, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventReminder> reminders = eventReminderRepository
                .findByUsernameAndIsSentTrueOrderByReminderTimeDesc(username, pageable);

        List<EventReminderResponse> responses = reminders.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<EventReminderResponse>(
                responses,
                reminders.getNumber(),
                reminders.getSize(),
                reminders.getTotalElements(),
                reminders.getTotalPages(),
                reminders.isFirst(),
                reminders.isLast()
        );
    }

    @Override
    @Transactional
    public List<EventReminderResponse> processRemindersToSend() {
        log.info("Processing reminders to send");

        LocalDateTime now = LocalDateTime.now();
        List<EventReminder> remindersToSend = eventReminderRepository.findRemindersToSend(now);

        List<EventReminderResponse> responses = remindersToSend.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        // Mark reminders as sent
        remindersToSend.forEach(reminder -> {
            reminder.setIsSent(true);
            eventReminderRepository.save(reminder);
        });

        return responses;
    }

    @Override
    @Transactional
    public EventReminderResponse markReminderAsSent(Long reminderId) {
        log.info("Marking reminder as sent: {}", reminderId);

        EventReminder reminder = eventReminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        reminder.setIsSent(true);
        EventReminder updatedReminder = eventReminderRepository.save(reminder);
        return mapToResponse(updatedReminder);
    }

    @Override
    public List<EventReminderResponse> getRemindersByType(String reminderType) {
        log.info("Getting reminders by type: {}", reminderType);

        EventReminder.ReminderType type = EventReminder.ReminderType.valueOf(reminderType.toUpperCase());
        List<EventReminder> reminders = eventReminderRepository.findByReminderTypeAndIsActiveTrueOrderByReminderTimeAsc(type);
        return reminders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventReminderResponse> getRemindersByChannel(String channel) {
        log.info("Getting reminders by channel: {}", channel);

        EventReminder.NotificationChannel notificationChannel = EventReminder.NotificationChannel.valueOf(channel.toUpperCase());
        List<EventReminder> reminders = eventReminderRepository.findByChannelAndIsActiveTrueOrderByReminderTimeAsc(notificationChannel);
        return reminders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private EventReminderResponse mapToResponse(EventReminder reminder) {
        return new EventReminderResponse(
                reminder.getId(),
                reminder.getEventId(),
                reminder.getUsername(),
                reminder.getReminderTime(),
                reminder.getReminderType().name(),
                reminder.getIsSent(),
                reminder.getIsActive(),
                reminder.getCustomMessage(),
                reminder.getChannel().name(),
                reminder.getCreatedAt(),
                reminder.getUpdatedAt()
        );
    }
} 