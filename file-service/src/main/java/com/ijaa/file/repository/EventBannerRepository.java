package com.ijaa.file.repository;

import com.ijaa.file.domain.entity.EventBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventBannerRepository extends JpaRepository<EventBanner, Long> {

    // Find banner by event ID (one banner per event)
    Optional<EventBanner> findByEventId(String eventId);

    // Check if event has a banner
    boolean existsByEventId(String eventId);
}
