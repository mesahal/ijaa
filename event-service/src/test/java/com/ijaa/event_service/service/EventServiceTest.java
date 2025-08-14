package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.entity.Event;
import com.ijaa.event_service.domain.request.EventRequest;
import com.ijaa.event_service.domain.response.EventResponse;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.common.client.UserServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private EventService eventService;

    private Event testEvent;
    private EventRequest eventRequest;

    @BeforeEach
    void setUp() {
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setTitle("Test Event");
        testEvent.setDescription("Test Event Description");
        testEvent.setLocation("Test Location");
        testEvent.setStartDate(LocalDateTime.now().plusDays(1));
        testEvent.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
        testEvent.setOrganizerName("Test Organizer");
        testEvent.setOrganizerEmail("organizer@test.com");
        testEvent.setPrivacy(Event.EventPrivacy.PUBLIC);
        testEvent.setMaxParticipants(100);
        testEvent.setActive(true);
        testEvent.setEventType("CONFERENCE");
        testEvent.setIsOnline(false);

        eventRequest = new EventRequest();
        eventRequest.setTitle("New Event");
        eventRequest.setDescription("New Event Description");
        eventRequest.setLocation("New Location");
        eventRequest.setStartDate(LocalDateTime.now().plusDays(2));
        eventRequest.setEndDate(LocalDateTime.now().plusDays(2).plusHours(3));
        eventRequest.setPrivacy("PUBLIC");
        eventRequest.setMaxParticipants(50);
        eventRequest.setOrganizerName("New Organizer");
        eventRequest.setOrganizerEmail("neworganizer@test.com");
        eventRequest.setEventType("WORKSHOP");
        eventRequest.setIsOnline(false);
    }

    @Test
    void shouldCreateEventWhenValidRequestProvided() {
        // Given
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        EventResponse result = eventService.createEvent(eventRequest);

        // Then
        assertNotNull(result);
        assertEquals("Test Event", result.getTitle());
        assertEquals("Test Event Description", result.getDescription());
        assertEquals("Test Location", result.getLocation());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldGetEventByIdWhenValidIdProvided() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(testEvent));

        // When
        EventResponse result = eventService.getEventById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test Event", result.getTitle());
        assertEquals("Test Event Description", result.getDescription());
        verify(eventRepository).findById(1L);
    }

    @Test
    void shouldGetAllEventsWhenCalled() {
        // Given
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findAll()).thenReturn(events);

        // When
        List<EventResponse> result = eventService.getAllEvents();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).getTitle());
        verify(eventRepository).findAll();
    }

    @Test
    void shouldGetActiveEventsWhenCalled() {
        // Given
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByActiveTrue()).thenReturn(events);

        // When
        List<EventResponse> result = eventService.getActiveEvents();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).getTitle());
        verify(eventRepository).findByActiveTrue();
    }

    @Test
    void shouldGetEventsByUserWhenValidUsernameProvided() {
        // Given
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByCreatedByUsername("testuser")).thenReturn(events);

        // When
        List<EventResponse> result = eventService.getEventsByUser("testuser");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).getTitle());
        verify(eventRepository).findByCreatedByUsername("testuser");
    }

    @Test
    void shouldUpdateEventWhenValidRequestProvided() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        EventResponse result = eventService.updateEvent(1L, eventRequest);

        // Then
        assertNotNull(result);
        assertEquals("Test Event", result.getTitle());
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldDeleteEventWhenValidIdProvided() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(testEvent));
        doNothing().when(eventRepository).delete(testEvent);

        // When
        eventService.deleteEvent(1L);

        // Then
        verify(eventRepository).findById(1L);
        verify(eventRepository).delete(testEvent);
    }

    @Test
    void shouldActivateEventWhenValidIdProvided() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        EventResponse result = eventService.activateEvent(1L);

        // Then
        assertNotNull(result);
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldDeactivateEventWhenValidIdProvided() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(testEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // When
        EventResponse result = eventService.deactivateEvent(1L);

        // Then
        assertNotNull(result);
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void shouldGetTotalEventsWhenCalled() {
        // Given
        when(eventRepository.count()).thenReturn(10L);

        // When
        Long result = eventService.getTotalEvents();

        // Then
        assertEquals(10L, result);
        verify(eventRepository).count();
    }

    @Test
    void shouldGetActiveEventsCountWhenCalled() {
        // Given
        when(eventRepository.countByActiveTrue()).thenReturn(5L);

        // When
        Long result = eventService.getActiveEventsCount();

        // Then
        assertEquals(5L, result);
        verify(eventRepository).countByActiveTrue();
    }

    @Test
    void shouldSearchEventsWhenValidCriteriaProvided() {
        // Given
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.searchEvents("Test Location", "CONFERENCE", 
            LocalDateTime.now(), LocalDateTime.now().plusDays(7), false, "Test Organizer", 
            "Test", "Description")).thenReturn(events);

        // When
        List<EventResponse> result = eventService.searchEvents("Test Location", "CONFERENCE", 
            LocalDateTime.now(), LocalDateTime.now().plusDays(7), false, "Test Organizer", 
            "Test", "Description");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).getTitle());
        verify(eventRepository).searchEvents("Test Location", "CONFERENCE", 
            LocalDateTime.now(), LocalDateTime.now().plusDays(7), false, "Test Organizer", 
            "Test", "Description");
    }
}
