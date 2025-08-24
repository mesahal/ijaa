package com.ijaa.event_service.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_reminders")
@EntityListeners(AuditingEntityListener.class)
public class EventReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false, length = 50)
    private String username; // Username of the user who set the reminder

    @Column(nullable = false)
    private LocalDateTime reminderTime; // When the reminder should be sent

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ReminderType reminderType = ReminderType.CUSTOM;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSent = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActive = true;

    @Column(length = 200)
    private String customMessage; // Custom reminder message

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private NotificationChannel channel = NotificationChannel.EMAIL;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum ReminderType {
        ONE_HOUR_BEFORE,
        ONE_DAY_BEFORE,
        ONE_WEEK_BEFORE,
        CUSTOM
    }

    public enum NotificationChannel {
        EMAIL,
        SMS,
        PUSH_NOTIFICATION,
        IN_APP
    }
} 