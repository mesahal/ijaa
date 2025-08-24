package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.entity.Event;
import com.ijaa.event_service.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event_service.domain.response.EventResponse;
import com.ijaa.event_service.repository.EventCommentRepository;
import com.ijaa.event_service.repository.EventMediaRepository;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.service.AdvancedEventSearchService;
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
    private final EventMediaRepository eventMediaRepository;

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
                .collect(Collectors.toList());

        // Apply engagement filters
        if (hasComments != null && hasComments) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> eventCommentRepository.countByEventId(event.getId()) > 0)
                    .collect(Collectors.toList());
        }

        if (hasMedia != null && hasMedia) {
            filteredEvents = filteredEvents.stream()
                    .filter(event -> eventMediaRepository.countByEventId(event.getId()) > 0)
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

    @Override
    public List<EventResponse> getEventRecommendations(String username) {
        log.info("Getting event recommendations for user: {}", username);

        // Simple recommendation logic - get recent active events
        // In a real app, you'd implement more sophisticated recommendation algorithms
        List<Event> events = eventRepository.findActiveEventsOrderByCreatedAtDesc(PageRequest.of(0, 10)).getContent();

        return events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getTrendingEvents(int limit) {
        log.info("Getting trending events, limit: {}", limit);

        // Simple trending logic - events with high participation
        // In a real app, you'd consider engagement metrics, social signals, etc.
        List<Event> events = eventRepository.findActiveEventsOrderByCurrentParticipantsDesc(PageRequest.of(0, limit)).getContent();

        return events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getEventsByLocation(String location, int limit) {
        log.info("Getting events by location: {}, limit: {}", location, limit);

        List<Event> events = eventRepository.findByLocationContainingAndActiveTrueOrderByStartDateAsc(location, PageRequest.of(0, limit));

        return events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getEventsByOrganizer(String organizerName, int limit) {
        log.info("Getting events by organizer: {}, limit: {}", organizerName, limit);

        List<Event> events = eventRepository.findByOrganizerNameContainingAndActiveTrueOrderByStartDateDesc(organizerName, PageRequest.of(0, limit));

        return events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getHighEngagementEvents(int limit) {
        log.info("Getting high engagement events, limit: {}", limit);

        // Events with high participation, comments, and media
        List<Event> events = eventRepository.findActiveEventsOrderByCurrentParticipantsDesc(PageRequest.of(0, limit)).getContent();

        // Filter for events with engagement
        List<Event> highEngagementEvents = events.stream()
                .filter(event -> {
                    long commentCount = eventCommentRepository.countByEventId(event.getId());
                    long mediaCount = eventMediaRepository.countByEventId(event.getId());
                    return event.getCurrentParticipants() > 5 || commentCount > 2 || mediaCount > 1;
                })
                .limit(limit)
                .collect(Collectors.toList());

        return highEngagementEvents.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getUpcomingEvents(String location, String eventType, int limit) {
        log.info("Getting upcoming events, location: {}, eventType: {}, limit: {}", location, eventType, limit);

        LocalDateTime now = LocalDateTime.now();
        List<Event> events = eventRepository.findByStartDateAfterAndActiveTrueOrderByStartDateAsc(now, PageRequest.of(0, limit)).getContent();

        // Apply filters
        List<Event> filteredEvents = events.stream()
                .filter(event -> location == null || event.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(event -> eventType == null || event.getEventType().equalsIgnoreCase(eventType))
                .limit(limit)
                .collect(Collectors.toList());

        return filteredEvents.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getSimilarEvents(Long eventId, int limit) {
        log.info("Getting similar events for event: {}, limit: {}", eventId, limit);

        Event sourceEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Find events with similar characteristics
        List<Event> similarEvents = eventRepository.findByEventTypeAndLocationAndActiveTrueAndIdNot(
                sourceEvent.getEventType(),
                sourceEvent.getLocation(),
                eventId,
                PageRequest.of(0, limit)
        );

        return similarEvents.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
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