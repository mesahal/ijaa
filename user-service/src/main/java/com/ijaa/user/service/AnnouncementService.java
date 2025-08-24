package com.ijaa.user.service;

import com.ijaa.user.domain.request.AnnouncementRequest;
import com.ijaa.user.domain.response.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    
    // Get all announcements
    List<AnnouncementResponse> getAllAnnouncements();
    
    // Get all active announcements
    List<AnnouncementResponse> getActiveAnnouncements();
    
    // Get announcement by ID
    AnnouncementResponse getAnnouncementById(Long announcementId);
    
    // Create new announcement
    AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest);
    
    // Update existing announcement
    AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest);
    
    // Delete announcement
    void deleteAnnouncement(Long announcementId);
    
    // Activate announcement
    AnnouncementResponse activateAnnouncement(Long announcementId);
    
    // Deactivate announcement
    AnnouncementResponse deactivateAnnouncement(Long announcementId);
    
    // Dashboard statistics
    Long getTotalAnnouncements();
    Long getActiveAnnouncementsCount();
} 