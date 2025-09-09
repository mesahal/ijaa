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
@Table(name = "event_participations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"eventId", "participantUsername"})
})
@EntityListeners(AuditingEntityListener.class)
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false, length = 50)
    private String participantUsername;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;

    @Column(length = 500)
    private String message; // Optional message from participant

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum ParticipationStatus {
        GOING,     // User confirmed attendance
        MAYBE,     // User might attend
        NOT_GOING, // User declined
        PENDING    // Invitation sent but not responded
    }
} 
