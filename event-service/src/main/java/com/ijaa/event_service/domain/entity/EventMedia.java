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
@Table(name = "event_media")
@EntityListeners(AuditingEntityListener.class)
public class EventMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false, length = 50)
    private String uploadedByUsername; // Username of the uploader

    @Column(nullable = false, length = 200)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String fileUrl; // URL to the stored file

    @Column(nullable = false, length = 50)
    private String fileType; // image/jpeg, video/mp4, etc.

    @Column(nullable = false)
    private Long fileSize; // File size in bytes

    @Column(length = 200)
    private String caption; // Optional caption for the media

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MediaType mediaType = MediaType.IMAGE;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isApproved = true; // For moderation

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer likes = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public enum MediaType {
        IMAGE,
        VIDEO,
        DOCUMENT
    }
} 