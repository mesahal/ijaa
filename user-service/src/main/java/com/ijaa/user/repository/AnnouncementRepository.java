package com.ijaa.user.repository;

import com.ijaa.user.domain.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    
    // Find all active announcements
    List<Announcement> findByActiveTrue();
    
    // Find all active announcements ordered by creation date (newest first)
    List<Announcement> findByActiveTrueOrderByCreatedAtDesc();
    
    // Find announcements by category
    List<Announcement> findByCategory(String category);
    
    // Find active announcements by category
    List<Announcement> findByCategoryAndActiveTrue(String category);
    
    // Find urgent announcements
    List<Announcement> findByIsUrgentTrue();
    
    // Find active urgent announcements
    List<Announcement> findByIsUrgentTrueAndActiveTrue();
    
    // Find announcements by author email
    List<Announcement> findByAuthorEmail(String authorEmail);
    
    // Find active announcements by author email
    List<Announcement> findByAuthorEmailAndActiveTrue(String authorEmail);
    
    // Dashboard statistics methods
    @Query("SELECT COUNT(a) FROM Announcement a WHERE a.active = true")
    Long countByActiveTrue();
    
    @Query("SELECT COUNT(a) FROM Announcement a WHERE a.active = false")
    Long countByActiveFalse();
    
    @Query("SELECT COUNT(a) FROM Announcement a WHERE a.isUrgent = true AND a.active = true")
    Long countUrgentActiveAnnouncements();
} 