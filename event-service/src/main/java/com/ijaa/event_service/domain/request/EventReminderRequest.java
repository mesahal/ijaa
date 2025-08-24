package com.ijaa.event_service.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventReminderRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "Reminder time is required")
    private LocalDateTime reminderTime;

    @NotNull(message = "Reminder type is required")
    private String reminderType; // ONE_HOUR_BEFORE, ONE_DAY_BEFORE, ONE_WEEK_BEFORE, CUSTOM

    @Size(max = 200, message = "Custom message must be less than 200 characters")
    private String customMessage;

    @NotNull(message = "Notification channel is required")
    private String channel; // EMAIL, SMS, PUSH_NOTIFICATION, IN_APP
} 