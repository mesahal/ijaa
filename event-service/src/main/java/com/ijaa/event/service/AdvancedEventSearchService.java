package com.ijaa.event.service;

import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event.domain.response.EventResponse;

public interface AdvancedEventSearchService {

    // Unified advanced search with multiple filters
    // Supports: location search, organizer search, upcoming events, and all combinations
    PagedResponse<EventResponse> searchEvents(AdvancedEventSearchRequest request);
} 
