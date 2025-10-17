package com.ijaa.event.repository;

import com.ijaa.event.domain.entity.EventPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EventPostRepository extends JpaRepository<EventPost, Long> {

    // Find all posts for an event
    Page<EventPost> findByEventIdAndIsDeletedFalseOrderByCreatedAtDesc(Long eventId, Pageable pageable);

    // Find posts by event and type
    Page<EventPost> findByEventIdAndPostTypeAndIsDeletedFalseOrderByCreatedAtDesc(
            Long eventId, EventPost.PostType postType, Pageable pageable);

    // Find posts by username
    Page<EventPost> findByUsernameAndIsDeletedFalseOrderByCreatedAtDesc(
            String username, Pageable pageable);

    // Find posts by username and event
    Page<EventPost> findByUsernameAndEventIdAndIsDeletedFalseOrderByCreatedAtDesc(
            String username, Long eventId, Pageable pageable);





    // Count posts for an event
    @Query("SELECT COUNT(p) FROM EventPost p WHERE p.eventId = :eventId AND p.isDeleted = false")
    Long countByEventId(@Param("eventId") Long eventId);

    // Count posts by username
    @Query("SELECT COUNT(p) FROM EventPost p WHERE p.username = :username AND p.isDeleted = false")
    Long countByUsername(@Param("username") String username);


}
