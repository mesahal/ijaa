package com.ijaa.event.service.impl;

import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event.domain.response.EventResponse;
import com.ijaa.event.repository.EventCommentRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.AdvancedEventSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvancedEventSearchServiceImpl implements AdvancedEventSearchService {

    private final EventRepository eventRepository;
    private final EventCommentRepository eventCommentRepository;

    @Override
    public PagedResponse<EventResponse> searchEvents(AdvancedEventSearchRequest request) {
        log.info("Advanced search for events with filters");

        // Create pageable with sorting
        Pageable pageable = createPageable(request);

        // Build search criteria
        String query = request.getQuery();
        String location = request.getLocation();
        String eventType = request.getEventType();
        LocalDateTime startDate = request.getStartDate();
        LocalDateTime endDate = request.getEndDate();
        String organizerName = request.getOrganizerName();
        Boolean isOnline = request.getIsOnline();
        String privacy = request.getPrivacy();
        Integer minParticipants = request.getMinParticipants();
        Integer maxParticipants = request.getMaxParticipants();
        Boolean hasComments = request.getHasComments();
        Boolean hasMedia = request.getHasMedia();

        // Perform search (simplified implementation - in real app, you'd use a more sophisticated search)
        Page<Event> events = eventRepository.findActiveEvents(pageable);

        // Apply filters
        LocalDateTime now = LocalDateTime.now();
        List<Event> filteredEvents = events.getContent().stream()
                .filter(event -> query == null || event.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        event.getDescription().toLowerCase().contains(query.toLowerCase()))
                .filter(event -> location == null || event.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(event -> eventType == null || event.getEventType().equalsIgnoreCase(eventType))
                .filter(event -> startDate == null || event.getStartDate().isAfter(startDate))
                .filter(event -> endDate == null || event.getEndDate().isBefore(endDate))
                .filter(event -> organizerName == null || event.getOrganizerName().toLowerCase().contains(organizerName.toLowerCase()))
                .filter(event -> isOnline == null || event.getIsOnline().equals(isOnline))
                .filter(event -> privacy == null || event.getPrivacy().name().equalsIgnoreCase(privacy))
                .filter(event -> minParticipants == null || event.getCurrentParticipants() >= minParticipants)
                .filter(event -> maxParticipants == null || event.getCurrentParticipants() <= maxParticipants)
                .filter(event -> !Boolean.TRUE.equals(request.getUpcomingOnly()) || event.getStartDate().isAfter(now))
                .collect(Collectors.toList());

        // Apply engagement filters
        // Note: Comment filtering removed as comments are now post-based
        // Comments are now associated with posts, not directly with events
        // if (hasComments != null && hasComments) {
        //     filteredEvents = filteredEvents.stream()
        //             .filter(event -> eventCommentRepository.countByEventId(event.getId()) > 0)
        //             .collect(Collectors.toList());
        // }

        // Note: Media filtering removed as we simplified to banner-only system
        // if (hasMedia != null && hasMedia) {
        //     filteredEvents = filteredEvents.stream()
        //             .filter(event -> eventBannerRepository.existsByEventId(event.getId()))
        //             .collect(Collectors.toList());
        // }

        // Apply limit if specified (for non-paginated results)
        if (request.getLimit() != null && request.getLimit() > 0) {
            filteredEvents = filteredEvents.stream()
                    .limit(request.getLimit())
                    .collect(Collectors.toList());
        }

        // Convert to responses
        List<EventResponse> responses = filteredEvents.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());

        return new PagedResponse<EventResponse>(
                responses,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                filteredEvents.size(),
                (int) Math.ceil((double) filteredEvents.size() / pageable.getPageSize()),
                pageable.getPageNumber() == 0,
                pageable.getPageNumber() >= (int) Math.ceil((double) filteredEvents.size() / pageable.getPageSize()) - 1
        );
    }

    private Pageable createPageable(AdvancedEventSearchRequest request) {
        Sort sort = Sort.by("createdAt").descending(); // default sort

        if (request.getSortBy() != null) {
            switch (request.getSortBy().toLowerCase()) {
                case "start_date":
                    sort = Sort.by("startDate").ascending();
                    break;
                case "popularity":
                    sort = Sort.by("currentParticipants").descending();
                    break;
                case "distance":
                    // In a real app, you'd implement distance-based sorting
                    sort = Sort.by("createdAt").descending();
                    break;
                default:
                    sort = Sort.by("createdAt").descending();
                    break;
            }
        }

        if ("desc".equalsIgnoreCase(request.getSortOrder())) {
            sort = sort.descending();
        }

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private EventResponse mapToEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getEventType(),
                event.getActive(),
                event.getIsOnline(),
                event.getMeetingLink(),
                event.getMaxParticipants(),
                event.getCurrentParticipants(),
                event.getOrganizerName(),
                event.getOrganizerEmail(),
                event.getCreatedByUsername(),
                event.getPrivacy().name(),
                event.getInviteMessage(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
} 
