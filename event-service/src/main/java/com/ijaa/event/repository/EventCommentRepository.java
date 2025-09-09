package com.ijaa.event.repository;

import com.ijaa.event.domain.entity.EventComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventCommentRepository extends JpaRepository<EventComment, Long> {

    // Find all comments for an event (top-level comments only)
    Page<EventComment> findByEventIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(
            Long eventId, Pageable pageable);

    // Find all comments for an event (including replies)
    List<EventComment> findByEventIdAndIsDeletedFalseOrderByCreatedAtAsc(Long eventId);

    // Find replies for a specific comment
    List<EventComment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(Long parentCommentId);

    // Count comments for an event
    @Query("SELECT COUNT(c) FROM EventComment c WHERE c.eventId = :eventId AND c.isDeleted = false")
    Long countByEventId(@Param("eventId") Long eventId);

    // Find comments by username
    Page<EventComment> findByUsernameAndIsDeletedFalseOrderByCreatedAtDesc(
            String username, Pageable pageable);

    // Find recent comments
    @Query("SELECT c FROM EventComment c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<EventComment> findRecentComments(Pageable pageable);

    // Find recent comments for a specific event
    @Query("SELECT c FROM EventComment c WHERE c.eventId = :eventId AND c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<EventComment> findRecentCommentsByEventId(@Param("eventId") Long eventId, Pageable pageable);

    // Find comments with high engagement (likes)
    @Query("SELECT c FROM EventComment c WHERE c.isDeleted = false ORDER BY c.likes DESC")
    Page<EventComment> findPopularComments(Pageable pageable);

    // Find popular comments for a specific event
    @Query("SELECT c FROM EventComment c WHERE c.eventId = :eventId AND c.isDeleted = false ORDER BY c.likes DESC")
    Page<EventComment> findPopularCommentsByEventId(@Param("eventId") Long eventId, Pageable pageable);
} 
