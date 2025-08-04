package com.ijaa.user.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.request.EventRequest;
import com.ijaa.user.domain.response.EventResponse;
import com.ijaa.user.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserEventResource.class)
class UserEventResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventRequest eventRequest;
    private EventResponse eventResponse;

    @BeforeEach
    void setUp() {
        eventRequest = new EventRequest();
        eventRequest.setTitle("Test Event");
        eventRequest.setDescription("Test Description");
        eventRequest.setStartDate(LocalDateTime.now().plusDays(1));
        eventRequest.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
        eventRequest.setLocation("Test Location");
        eventRequest.setEventType("MEETING");
        eventRequest.setMaxParticipants(100);
        eventRequest.setOrganizerName("Test Organizer");
        eventRequest.setOrganizerEmail("test@example.com");

        eventResponse = new EventResponse();
        eventResponse.setId(1L);
        eventResponse.setTitle("Test Event");
        eventResponse.setDescription("Test Description");
        eventResponse.setStartDate(LocalDateTime.now().plusDays(1));
        eventResponse.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
        eventResponse.setLocation("Test Location");
        eventResponse.setEventType("MEETING");
        eventResponse.setActive(true);
        eventResponse.setIsOnline(false);
        eventResponse.setMaxParticipants(100);
        eventResponse.setCurrentParticipants(0);
        eventResponse.setOrganizerName("Test Organizer");
        eventResponse.setOrganizerEmail("test@example.com");
        eventResponse.setCreatedByUsername("testuser");
        eventResponse.setCreatedAt(LocalDateTime.now());
        eventResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUserEvents() throws Exception {
        List<EventResponse> events = Arrays.asList(eventResponse);
        when(eventService.getEventsByUser("testuser")).thenReturn(events);

        mockMvc.perform(get("/api/v1/user/events/my-events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User events retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Event"))
                .andExpect(jsonPath("$.data[0].createdByUsername").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateEvent() throws Exception {
        when(eventService.createEventForUser(any(EventRequest.class), eq("testuser")))
                .thenReturn(eventResponse);

        mockMvc.perform(post("/api/v1/user/events/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event created successfully"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Event"))
                .andExpect(jsonPath("$.data.createdByUsername").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllActiveEvents() throws Exception {
        List<EventResponse> events = Arrays.asList(eventResponse);
        when(eventService.getActiveEvents()).thenReturn(events);

        mockMvc.perform(get("/api/v1/user/events/all-events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All active events retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Test Event"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetEventById() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(eventResponse);

        mockMvc.perform(get("/api/v1/user/events/all-events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Event"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateUserEvent() throws Exception {
        when(eventService.updateEventForUser(eq(1L), any(EventRequest.class), eq("testuser")))
                .thenReturn(eventResponse);

        mockMvc.perform(put("/api/v1/user/events/my-events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event updated successfully"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Event"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteUserEvent() throws Exception {
        mockMvc.perform(delete("/api/v1/user/events/my-events/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Event deleted successfully"))
                .andExpect(jsonPath("$.code").value("200"));
    }
} 