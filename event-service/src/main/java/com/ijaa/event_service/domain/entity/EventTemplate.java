package com.ijaa.event_service.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "event_templates")
@EntityListeners(AuditingEntityListener.class)
public class EventTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false, length = 200)
    private String name;

    @Column(name = "name", nullable = false, length = 200)
    private String templateName;

    @Column(nullable = false, length = 50)
    private String createdByUsername;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TemplateCategory category;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isPublic = false; // Whether template is available to all users

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    // Template Content
    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String location;

    @Column(length = 50)
    private String eventType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isOnline = false;

    @Column(length = 500)
    private String meetingLink;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(length = 100)
    private String organizerName;

    @Column(length = 100)
    private String organizerEmail;

    @Column(length = 500)
    private String inviteMessage;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EventPrivacy privacy = EventPrivacy.PUBLIC;

    // Default Duration (in minutes)
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 60")
    private Integer defaultDurationMinutes = 60;

    // Default Time
    @Column
    private LocalTime defaultStartTime;

    @Column
    private LocalTime defaultEndTime;

    // Recurring Template Settings
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean supportsRecurrence = false;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private RecurrenceType defaultRecurrenceType;

    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer defaultRecurrenceInterval = 1;

    @Column(length = 100)
    private String defaultRecurrenceDays;

    // Template Usage Statistics
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer usageCount = 0;

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double averageRating = 0.0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalRatings = 0;

    @Column(length = 500)
    private String tags; // Comma-separated tags for search

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum TemplateCategory {
        CONFERENCE,
        WORKSHOP,
        MEETING,
        NETWORKING,
        SEMINAR,
        WEBINAR,
        SOCIAL,
        ACADEMIC,
        PROFESSIONAL,
        CAREER,
        REUNION,
        SPORTS,
        MENTORSHIP,
        OTHER
    }

    public enum RecurrenceType {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public enum EventPrivacy {
        PUBLIC,
        PRIVATE,
        INVITE_ONLY
    }
} 