package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.entity.Event;
import com.ijaa.event_service.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event_service.domain.response.EventResponse;
import com.ijaa.event_service.repository.EventCommentRepository;
import com.ijaa.event_service.repository.EventMediaRepository;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.service.impl.AdvancedEventSearchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvancedEventSearchServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventCommentRepository eventCommentRepository;

    @Mock
    private EventMediaRepository eventMediaRepository;

    @InjectMocks
    private AdvancedEventSearchServiceImpl advancedEventSearchService;

    private Event testEvent1;
    private Event testEvent2;
    private Event testEvent3;

    @BeforeEach
    void setUp() {
        testEvent1 = new Event();
        testEvent1.setId(1L);
        testEvent1.setTitle("Alumni Meet 2024");
        testEvent1.setDescription("Annual alumni gathering");
        testEvent1.setStartDate(LocalDateTime.now().plusDays(7));
        testEvent1.setEndDate(LocalDateTime.now().plusDays(7).plusHours(4));
        testEvent1.setLocation("IIT Campus");
        testEvent1.setEventType("MEETING");
        testEvent1.setActive(true);
        testEvent1.setIsOnline(false);
        testEvent1.setMaxParticipants(100);
        testEvent1.setCurrentParticipants(45);
        testEvent1.setOrganizerName("John Doe");
        testEvent1.setOrganizerEmail("john@example.com");
        testEvent1.setCreatedByUsername("johndoe");
        testEvent1.setPrivacy(Event.EventPrivacy.PUBLIC);
        testEvent1.setInviteMessage("Join us for the annual alumni meet");

        testEvent2 = new Event();
        testEvent2.setId(2L);
        testEvent2.setTitle("Tech Webinar");
        testEvent2.setDescription("Technology innovation webinar");
        testEvent2.setStartDate(LocalDateTime.now().plusDays(14));
        testEvent2.setEndDate(LocalDateTime.now().plusDays(14).plusHours(2));
        testEvent2.setLocation("Virtual");
        testEvent2.setEventType("WEBINAR");
        testEvent2.setActive(true);
        testEvent2.setIsOnline(true);
        testEvent2.setMaxParticipants(200);
        testEvent2.setCurrentParticipants(150);
        testEvent2.setOrganizerName("Jane Smith");
        testEvent2.setOrganizerEmail("jane@example.com");
        testEvent2.setCreatedByUsername("janesmith");
        testEvent2.setPrivacy(Event.EventPrivacy.PUBLIC);
        testEvent2.setInviteMessage("Join our tech webinar");

        testEvent3 = new Event();
        testEvent3.setId(3L);
        testEvent3.setTitle("Career Workshop");
        testEvent3.setDescription("Career development workshop");
        testEvent3.setStartDate(LocalDateTime.now().plusDays(21));
        testEvent3.setEndDate(LocalDateTime.now().plusDays(21).plusHours(6));
        testEvent3.setLocation("IIT Campus");
        testEvent3.setEventType("WORKSHOP");
        testEvent3.setActive(true);
        testEvent3.setIsOnline(false);
        testEvent3.setMaxParticipants(50);
        testEvent3.setCurrentParticipants(30);
        testEvent3.setOrganizerName("Mike Johnson");
        testEvent3.setOrganizerEmail("mike@example.com");
        testEvent3.setCreatedByUsername("mikejohnson");
        testEvent3.setPrivacy(Event.EventPrivacy.PUBLIC);
        testEvent3.setInviteMessage("Career development workshop");
    }

    @Test
    void testSearchEvents_WithLocationFilter_ShouldReturnFilteredEvents() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setLocation("IIT Campus");
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent3));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Alumni Meet 2024", result.getContent().get(0).getTitle());
        assertEquals("Career Workshop", result.getContent().get(1).getTitle());
        verify(eventRepository).findActiveEvents(any(Pageable.class));
    }

    @Test
    void testSearchEvents_WithEventTypeFilter_ShouldReturnFilteredEvents() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setEventType("WEBINAR");
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent2));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Tech Webinar", result.getContent().get(0).getTitle());
        assertEquals("WEBINAR", result.getContent().get(0).getEventType());
    }

    @Test
    void testSearchEvents_WithOnlineFilter_ShouldReturnFilteredEvents() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setIsOnline(true);
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent2));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Tech Webinar", result.getContent().get(0).getTitle());
        assertTrue(result.getContent().get(0).getIsOnline());
    }

    @Test
    void testSearchEvents_WithQueryFilter_ShouldReturnFilteredEvents() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setQuery("Alumni");
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Alumni Meet 2024", result.getContent().get(0).getTitle());
    }

    @Test
    void testSearchEvents_WithDateRangeFilter_ShouldReturnFilteredEvents() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setStartDate(LocalDateTime.now().plusDays(5));
        request.setEndDate(LocalDateTime.now().plusDays(15));
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent2));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    void testSearchEvents_WithPagination_ShouldReturnPaginatedResults() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setPage(0);
        request.setSize(2);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent2), PageRequest.of(0, 2), 3);
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
    }

    @Test
    void testGetEventRecommendations_ShouldReturnRecommendations() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent2, testEvent3));
        when(eventRepository.findActiveEventsOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getEventRecommendations("testuser");

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(eventRepository).findActiveEventsOrderByCreatedAtDesc(any(Pageable.class));
    }

    @Test
    void testGetTrendingEvents_ShouldReturnTrendingEvents() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent2, testEvent1, testEvent3));
        when(eventRepository.findActiveEventsOrderByCurrentParticipantsDesc(any(Pageable.class))).thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getTrendingEvents(5);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Tech Webinar", result.get(0).getTitle()); // Should be first due to highest participants
        verify(eventRepository).findActiveEventsOrderByCurrentParticipantsDesc(any(Pageable.class));
    }

    @Test
    void testGetEventsByLocation_ShouldReturnEventsInLocation() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent3));
        when(eventRepository.findByLocationContainingIgnoreCaseAndActiveTrueOrderByStartDateAsc(eq("IIT Campus"), any(Pageable.class)))
                .thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getEventsByLocation("IIT Campus", 10);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("IIT Campus", result.get(0).getLocation());
        assertEquals("IIT Campus", result.get(1).getLocation());
        verify(eventRepository).findByLocationContainingIgnoreCaseAndActiveTrueOrderByStartDateAsc(eq("IIT Campus"), any(Pageable.class));
    }

    @Test
    void testGetEventsByOrganizer_ShouldReturnEventsByOrganizer() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1));
        when(eventRepository.findByOrganizerNameContainingIgnoreCaseAndActiveTrueOrderByStartDateDesc(eq("John Doe"), any(Pageable.class)))
                .thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getEventsByOrganizer("John Doe", 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getOrganizerName());
        verify(eventRepository).findByOrganizerNameContainingIgnoreCaseAndActiveTrueOrderByStartDateDesc(eq("John Doe"), any(Pageable.class));
    }

    @Test
    void testGetHighEngagementEvents_ShouldReturnHighEngagementEvents() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent2, testEvent1, testEvent3));
        when(eventRepository.findActiveEventsOrderByCurrentParticipantsDesc(any(Pageable.class))).thenReturn(eventPage);
        when(eventCommentRepository.countByEventId(2L)).thenReturn(5L);
        when(eventCommentRepository.countByEventId(1L)).thenReturn(3L);
        when(eventCommentRepository.countByEventId(3L)).thenReturn(1L);
        when(eventMediaRepository.countByEventId(2L)).thenReturn(2L);
        when(eventMediaRepository.countByEventId(1L)).thenReturn(1L);
        when(eventMediaRepository.countByEventId(3L)).thenReturn(0L);

        // When
        List<EventResponse> result = advancedEventSearchService.getHighEngagementEvents(10);

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);
        verify(eventRepository).findActiveEventsOrderByCurrentParticipantsDesc(any(Pageable.class));
        verify(eventCommentRepository, atLeastOnce()).countByEventId(anyLong());
        verify(eventMediaRepository, atLeastOnce()).countByEventId(anyLong());
    }

    @Test
    void testGetUpcomingEvents_ShouldReturnUpcomingEvents() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent2, testEvent3));
        when(eventRepository.findByStartDateAfterAndActiveTrueOrderByStartDateAsc(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getUpcomingEvents(null, null, 10);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(eventRepository).findByStartDateAfterAndActiveTrueOrderByStartDateAsc(any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void testGetUpcomingEvents_WithLocationFilter_ShouldReturnFilteredEvents() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent3));
        when(eventRepository.findByStartDateAfterAndActiveTrueOrderByStartDateAsc(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getUpcomingEvents("IIT Campus", null, 10);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("IIT Campus", result.get(0).getLocation());
        assertEquals("IIT Campus", result.get(1).getLocation());
    }

    @Test
    void testGetUpcomingEvents_WithEventTypeFilter_ShouldReturnFilteredEvents() {
        // Given
        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent2));
        when(eventRepository.findByStartDateAfterAndActiveTrueOrderByStartDateAsc(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(eventPage);

        // When
        List<EventResponse> result = advancedEventSearchService.getUpcomingEvents(null, "WEBINAR", 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("WEBINAR", result.get(0).getEventType());
    }

    @Test
    void testGetSimilarEvents_ShouldReturnSimilarEvents() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent1));
        when(eventRepository.findByEventTypeAndLocationAndActiveTrueAndIdNot(eq("MEETING"), eq("IIT Campus"), eq(1L), any(Pageable.class)))
                .thenReturn(Arrays.asList(testEvent3));

        // When
        List<EventResponse> result = advancedEventSearchService.getSimilarEvents(1L, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Career Workshop", result.get(0).getTitle());
        verify(eventRepository).findById(1L);
        verify(eventRepository).findByEventTypeAndLocationAndActiveTrueAndIdNot(eq("MEETING"), eq("IIT Campus"), eq(1L), any(Pageable.class));
    }

    @Test
    void testGetSimilarEvents_WithNonExistentEvent_ShouldThrowException() {
        // Given
        when(eventRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            advancedEventSearchService.getSimilarEvents(999L, 10);
        });
        verify(eventRepository).findById(999L);
    }

    @Test
    void testSearchEvents_WithSorting_ShouldReturnSortedResults() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setSortBy("start_date");
        request.setSortOrder("asc");
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent2, testEvent3));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        verify(eventRepository).findActiveEvents(any(Pageable.class));
    }

    @Test
    void testSearchEvents_WithEngagementFilters_ShouldReturnFilteredEvents() {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setHasComments(true);
        request.setHasMedia(true);
        request.setPage(0);
        request.setSize(10);

        Page<Event> eventPage = new PageImpl<>(Arrays.asList(testEvent1, testEvent2, testEvent3));
        when(eventRepository.findActiveEvents(any(Pageable.class))).thenReturn(eventPage);
        when(eventCommentRepository.countByEventId(anyLong())).thenReturn(5L);
        when(eventMediaRepository.countByEventId(anyLong())).thenReturn(2L);

        // When
        PagedResponse<EventResponse> result = advancedEventSearchService.searchEvents(request);

        // Then
        assertNotNull(result);
        assertTrue(result.getContent().size() > 0);
        verify(eventRepository).findActiveEvents(any(Pageable.class));
        verify(eventCommentRepository, atLeastOnce()).countByEventId(anyLong());
        verify(eventMediaRepository, atLeastOnce()).countByEventId(anyLong());
    }
}
