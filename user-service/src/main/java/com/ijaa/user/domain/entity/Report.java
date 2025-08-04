package com.ijaa.user.domain.entity;

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
@Table(name = "reports")
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String category; // USER_REPORT, CONTENT_REPORT, BUG_REPORT, FEATURE_REQUEST, etc.

    @Column(length = 20)
    private String status; // PENDING, IN_PROGRESS, RESOLVED, CLOSED

    @Column(length = 20)
    private String priority; // LOW, MEDIUM, HIGH, URGENT

    @Column(length = 100)
    private String reporterName;

    @Column(length = 100)
    private String reporterEmail;

    @Column(length = 100)
    private String assignedTo;

    @Column(columnDefinition = "TEXT")
    private String adminNotes;

    @Column(length = 500)
    private String attachmentUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
} 