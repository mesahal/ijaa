package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.EventMediaRequest;
import com.ijaa.event_service.domain.response.EventMediaResponse;

import java.util.List;

public interface EventMediaService {

    // Upload media for an event
    EventMediaResponse uploadMedia(EventMediaRequest request, String username);

    // Get all media for an event
    List<EventMediaResponse> getEventMedia(Long eventId);

    // Get media by type for an event
    List<EventMediaResponse> getEventMediaByType(Long eventId, String mediaType);

    // Get a specific media item
    EventMediaResponse getMedia(Long mediaId);

    // Update media caption
    EventMediaResponse updateMediaCaption(Long mediaId, String caption, String username);

    // Delete media
    void deleteMedia(Long mediaId, String username);

    // Like/unlike media
    EventMediaResponse toggleLike(Long mediaId, String username);

    // Get user's uploaded media
    PagedResponse<EventMediaResponse> getUserMedia(String username, int page, int size);

    // Get recent media
    PagedResponse<EventMediaResponse> getRecentMedia(int page, int size);

    // Get popular media
    PagedResponse<EventMediaResponse> getPopularMedia(int page, int size);

    // Approve/reject media (admin only)
    EventMediaResponse approveMedia(Long mediaId, boolean approved);

    // Get pending approval media (admin only)
    PagedResponse<EventMediaResponse> getPendingApprovalMedia(int page, int size);
} 