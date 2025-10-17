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

    // Find all comments for a post (top-level comments only)
    Page<EventComment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(
            Long postId, Pageable pageable);

    // Find all comments for a post (including replies)
    List<EventComment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(Long postId);

    // Find replies for a specific comment
    List<EventComment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(Long parentCommentId);

    // Count comments for a post
    @Query("SELECT COUNT(c) FROM EventComment c WHERE c.postId = :postId AND c.isDeleted = false")
    Long countByPostId(@Param("postId") Long postId);

    // Find comments by username
    Page<EventComment> findByUsernameAndIsDeletedFalseOrderByCreatedAtDesc(
            String username, Pageable pageable);

    // Find recent comments
    @Query("SELECT c FROM EventComment c WHERE c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<EventComment> findRecentComments(Pageable pageable);

    // Find recent comments for a specific post
    @Query("SELECT c FROM EventComment c WHERE c.postId = :postId AND c.isDeleted = false ORDER BY c.createdAt DESC")
    Page<EventComment> findRecentCommentsByPostId(@Param("postId") Long postId, Pageable pageable);

    // Find comments with high engagement (likes)
    @Query("SELECT c FROM EventComment c WHERE c.isDeleted = false ORDER BY c.likes DESC")
    Page<EventComment> findPopularComments(Pageable pageable);

    // Find comments with high engagement for a specific post
    @Query("SELECT c FROM EventComment c WHERE c.postId = :postId AND c.isDeleted = false ORDER BY c.likes DESC")
    Page<EventComment> findPopularCommentsByPostId(@Param("postId") Long postId, Pageable pageable);
} 
