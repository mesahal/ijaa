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
@Table(name = "calendar_integrations")
@EntityListeners(AuditingEntityListener.class)
public class CalendarIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private CalendarType calendarType;

    @Column(nullable = false, length = 100)
    private String calendarName;

    @Column(length = 500)
    private String calendarUrl;

    @Column(length = 500)
    private String accessToken;

    @Column(length = 500)
    private String refreshToken;

    @Column
    private LocalDateTime tokenExpiry;

    @Column(length = 100)
    private String calendarId; // External calendar ID

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean syncToExternal = false; // Sync IJAA events to external calendar

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean syncFromExternal = false; // Sync external events to IJAA

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean syncRecurringEvents = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean syncReminders = false;

    @Column(length = 500)
    private String lastSyncError;

    @Column
    private LocalDateTime lastSyncTime;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer syncFrequencyHours = 24; // How often to sync

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum CalendarType {
        GOOGLE_CALENDAR,
        OUTLOOK_CALENDAR,
        APPLE_CALENDAR,
        OTHER
    }
} 