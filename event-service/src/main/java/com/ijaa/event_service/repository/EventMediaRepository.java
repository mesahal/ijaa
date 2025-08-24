package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventMedia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventMediaRepository extends JpaRepository<EventMedia, Long> {

    // Find all media for an event
    List<EventMedia> findByEventIdAndIsApprovedTrueOrderByCreatedAtDesc(Long eventId);

    // Find media by type for an event
    List<EventMedia> findByEventIdAndMediaTypeAndIsApprovedTrueOrderByCreatedAtDesc(
            Long eventId, EventMedia.MediaType mediaType);

    // Find media by uploader
    Page<EventMedia> findByUploadedByUsernameAndIsApprovedTrueOrderByCreatedAtDesc(
            String uploadedByUsername, Pageable pageable);

    // Count media for an event
    @Query("SELECT COUNT(m) FROM EventMedia m WHERE m.eventId = :eventId AND m.isApproved = true")
    Long countByEventId(@Param("eventId") Long eventId);

    // Find recent media
    @Query("SELECT m FROM EventMedia m WHERE m.isApproved = true ORDER BY m.createdAt DESC")
    Page<EventMedia> findRecentMedia(Pageable pageable);

    // Find popular media (by likes)
    @Query("SELECT m FROM EventMedia m WHERE m.isApproved = true ORDER BY m.likes DESC")
    Page<EventMedia> findPopularMedia(Pageable pageable);

    // Find media by file type
    List<EventMedia> findByFileTypeContainingAndIsApprovedTrueOrderByCreatedAtDesc(String fileType);

    // Find pending approval media (for admin)
    @Query("SELECT m FROM EventMedia m WHERE m.isApproved = false ORDER BY m.createdAt DESC")
    Page<EventMedia> findPendingApproval(Pageable pageable);
} 