package com.ijaa.event.service;

import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event.domain.response.EventResponse;

import java.util.List;

public interface AdvancedEventSearchService {

    // Advanced search with multiple filters
    PagedResponse<EventResponse> searchEvents(AdvancedEventSearchRequest request);

    // Get event recommendations for a user
    List<EventResponse> getEventRecommendations(String username);

    // Get trending events
    List<EventResponse> getTrendingEvents(int limit);

    // Get events by location
    List<EventResponse> getEventsByLocation(String location, int limit);

    // Get events by organizer
    List<EventResponse> getEventsByOrganizer(String organizerName, int limit);

    // Get events with high engagement (comments, media, participants)
    List<EventResponse> getHighEngagementEvents(int limit);

    // Get upcoming events with filters
    List<EventResponse> getUpcomingEvents(String location, String eventType, int limit);

    // Get similar events based on event type and location
    List<EventResponse> getSimilarEvents(Long eventId, int limit);
} 
