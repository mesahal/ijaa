package com.ijaa.event.domain.entity;

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
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
public class Event {

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
    private String eventType; // CONFERENCE, WORKSHOP, MEETING, etc.

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EventPrivacy privacy = EventPrivacy.PUBLIC;

    @Column(length = 500)
    private String inviteMessage; // Default message for invitations

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isOnline = false;

    @Column(length = 500)
    private String meetingLink; // For online events

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer currentParticipants = 0;

    @Column(length = 100)
    private String organizerName;

    @Column(length = 100)
    private String organizerEmail;

    // User association for tracking who created the event
    @Column(length = 50)
    private String createdByUsername; // Username of the user who created the event

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum EventPrivacy {
        PUBLIC,     // Anyone can see and join
        PRIVATE,    // Only invited users can see and join
        INVITE_ONLY // Anyone can see but only invited users can join
    }
}
