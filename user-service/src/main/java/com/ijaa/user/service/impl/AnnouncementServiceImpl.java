package com.ijaa.user.service.impl;

import com.ijaa.user.domain.entity.Announcement;
import com.ijaa.user.domain.request.AnnouncementRequest;
import com.ijaa.user.domain.response.AnnouncementResponse;
import com.ijaa.user.repository.AnnouncementRepository;
import com.ijaa.user.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {
        return announcementRepository.findAll().stream()
                .map(this::createAnnouncementResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AnnouncementResponse> getActiveAnnouncements() {
        return announcementRepository.findByActiveTrueOrderByCreatedAtDesc().stream()
                .map(this::createAnnouncementResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AnnouncementResponse getAnnouncementById(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + announcementId));
        
        return createAnnouncementResponse(announcement);
    }

    @Override
    public AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setContent(announcementRequest.getContent());
        announcement.setCategory(announcementRequest.getCategory());
        announcement.setActive(true);
        announcement.setIsUrgent(announcementRequest.getIsUrgent() != null ? announcementRequest.getIsUrgent() : false);
        announcement.setAuthorName(announcementRequest.getAuthorName());
        announcement.setAuthorEmail(announcementRequest.getAuthorEmail());
        announcement.setImageUrl(announcementRequest.getImageUrl());
        announcement.setViewCount(0);
        
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return createAnnouncementResponse(savedAnnouncement);
    }

    @Override
    public AnnouncementResponse updateAnnouncement(Long announcementId, AnnouncementRequest announcementRequest) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + announcementId));
        
        // Update announcement fields
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setContent(announcementRequest.getContent());
        announcement.setCategory(announcementRequest.getCategory());
        announcement.setIsUrgent(announcementRequest.getIsUrgent() != null ? announcementRequest.getIsUrgent() : false);
        announcement.setAuthorName(announcementRequest.getAuthorName());
        announcement.setAuthorEmail(announcementRequest.getAuthorEmail());
        announcement.setImageUrl(announcementRequest.getImageUrl());
        
        Announcement updatedAnnouncement = announcementRepository.save(announcement);
        return createAnnouncementResponse(updatedAnnouncement);
    }

    @Override
    public void deleteAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + announcementId));
        
        announcementRepository.delete(announcement);
    }

    @Override
    public AnnouncementResponse activateAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + announcementId));
        
        announcement.setActive(true);
        Announcement updatedAnnouncement = announcementRepository.save(announcement);
        return createAnnouncementResponse(updatedAnnouncement);
    }

    @Override
    public AnnouncementResponse deactivateAnnouncement(Long announcementId) {
        Announcement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new RuntimeException("Announcement not found with id: " + announcementId));
        
        announcement.setActive(false);
        Announcement updatedAnnouncement = announcementRepository.save(announcement);
        return createAnnouncementResponse(updatedAnnouncement);
    }

    @Override
    public Long getTotalAnnouncements() {
        return announcementRepository.count();
    }

    @Override
    public Long getActiveAnnouncementsCount() {
        return announcementRepository.countByActiveTrue();
    }

    private AnnouncementResponse createAnnouncementResponse(Announcement announcement) {
        AnnouncementResponse response = new AnnouncementResponse();
        response.setId(announcement.getId());
        response.setTitle(announcement.getTitle());
        response.setContent(announcement.getContent());
        response.setCategory(announcement.getCategory());
        response.setActive(announcement.getActive());
        response.setIsUrgent(announcement.getIsUrgent());
        response.setAuthorName(announcement.getAuthorName());
        response.setAuthorEmail(announcement.getAuthorEmail());
        response.setImageUrl(announcement.getImageUrl());
        response.setViewCount(announcement.getViewCount());
        response.setCreatedAt(announcement.getCreatedAt());
        response.setUpdatedAt(announcement.getUpdatedAt());
        return response;
    }
} 