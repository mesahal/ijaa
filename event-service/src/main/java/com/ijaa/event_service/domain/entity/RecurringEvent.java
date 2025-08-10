package com.ijaa.event_service.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recurring_events")
@EntityListeners(AuditingEntityListener.class)
public class RecurringEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(length = 100)
    private String location;

    @Column(length = 50)
    private String eventType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EventPrivacy privacy = EventPrivacy.PUBLIC;

    @Column(length = 500)
    private String inviteMessage;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isOnline = false;

    @Column(length = 500)
    private String meetingLink;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer currentParticipants = 0;

    @Column(length = 100)
    private String organizerName;

    @Column(length = 100)
    private String organizerEmail;

    @Column(length = 50)
    private String createdByUsername;

    // Recurring Event Specific Fields
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    @Column(nullable = false)
    private Integer recurrenceInterval = 1; // Every X days/weeks/months

    @Column(nullable = false)
    private LocalDateTime recurrenceEndDate;

    @Column(length = 100)
    private String recurrenceDays; // Comma-separated days for weekly recurrence (MONDAY,TUESDAY)

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer maxOccurrences = 0; // 0 means unlimited

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean generateInstances = true; // Whether to generate individual event instances

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

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