package com.ijaa.event_service.repository;

import com.ijaa.event_service.domain.entity.EventCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventCommentLikeRepository extends JpaRepository<EventCommentLike, Long> {

    // Check if user has liked a specific comment
    Optional<EventCommentLike> findByCommentIdAndUsername(Long commentId, String username);

    // Count likes for a specific comment
    @Query("SELECT COUNT(l) FROM EventCommentLike l WHERE l.commentId = :commentId")
    Long countByCommentId(@Param("commentId") Long commentId);

    // Get all likes for a specific comment
    List<EventCommentLike> findByCommentId(Long commentId);

    // Delete like by comment ID and username
    void deleteByCommentIdAndUsername(Long commentId, String username);

    // Check if user has liked a comment
    boolean existsByCommentIdAndUsername(Long commentId, String username);
}
