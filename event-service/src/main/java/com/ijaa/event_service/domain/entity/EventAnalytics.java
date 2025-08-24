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
@Table(name = "event_analytics")
@EntityListeners(AuditingEntityListener.class)
public class EventAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false, length = 50)
    private String eventTitle;

    @Column(nullable = false, length = 50)
    private String organizerUsername;

    // Attendance Tracking
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalInvitations = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer confirmedAttendees = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer actualAttendees = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer noShows = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer maybeAttendees = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer declinedAttendees = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer pendingResponses = 0;

    // Engagement Metrics
    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalComments = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalMediaUploads = 0;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalReminders = 0;

    // Response Time Metrics
    @Column
    private LocalDateTime firstRsvpTime;

    @Column
    private LocalDateTime lastRsvpTime;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer averageResponseTimeHours = 0;

    // Event Performance
    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double attendanceRate = 0.0; // Percentage of confirmed attendees vs total invitations

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double responseRate = 0.0; // Percentage of responses vs total invitations

    @Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double engagementRate = 0.0; // Percentage of attendees who engaged (comments, media, etc.)

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isCompleted = false;

    @Column
    private LocalDateTime eventStartDate;

    @Column
    private LocalDateTime eventEndDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;
} 