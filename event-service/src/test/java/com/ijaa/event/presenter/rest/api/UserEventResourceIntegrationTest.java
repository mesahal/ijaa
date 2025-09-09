package com.ijaa.event.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.request.EventRequest;
import com.ijaa.event.domain.request.EventSearchRequest;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserEventResourceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Event testEvent;
    private String testUsername = "testuser";
    private String userContextHeader;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        // Create test event
        EventRequest eventRequest = new EventRequest();
        eventRequest.setTitle("Test Event");
        eventRequest.setDescription("Test Event Description");
        eventRequest.setStartDate(LocalDateTime.now().plusDays(1));
        eventRequest.setEndDate(LocalDateTime.now().plusDays(1).plusHours(2));
        eventRequest.setLocation("Test Location");
        eventRequest.setEventType("MEETING");
        eventRequest.setIsOnline(false);
        eventRequest.setMaxParticipants(100);
        eventRequest.setOrganizerName("Test Organizer");
        eventRequest.setOrganizerEmail("organizer@test.com");

        testEvent = eventRepository.save(createEventFromRequest(eventRequest, testUsername));

        // Create user context header
        String userContextJson = String.format(
            "{\"username\":\"%s\",\"userType\":\"USER\",\"role\":\"USER\"}", 
            testUsername
        );
        userContextHeader = Base64.getUrlEncoder().encodeToString(userContextJson.getBytes());
    }

    @Test
    void testSearchEvents_Success() throws Exception {
        EventSearchRequest request = new EventSearchRequest();
        request.setLocation("Test Location");
        request.setEventType("MEETING");
        request.setIsOnline(false);

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(testEvent.getId()))
                .andExpect(jsonPath("$.data[0].title").value("Test Event"))
                .andExpect(jsonPath("$.data[0].location").value("Test Location"))
                .andExpect(jsonPath("$.data[0].eventType").value("MEETING"));
    }

    @Test
    void testSearchEvents_ByTitle() throws Exception {
        EventSearchRequest request = new EventSearchRequest();
        request.setTitle("Test Event");

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].title").value("Test Event"));
    }

    @Test
    void testSearchEvents_ByOrganizer() throws Exception {
        EventSearchRequest request = new EventSearchRequest();
        request.setOrganizerName("Test Organizer");

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].organizerName").value("Test Organizer"));
    }

    @Test
    void testSearchEvents_ByDescription() throws Exception {
        EventSearchRequest request = new EventSearchRequest();
        request.setDescription("Test Event Description");

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].description").value("Test Event Description"));
    }

    @Test
    void testSearchEvents_ByDateRange() throws Exception {
        EventSearchRequest request = new EventSearchRequest();
        request.setStartDate(LocalDateTime.now());
        request.setEndDate(LocalDateTime.now().plusDays(2));

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testSearchEvents_ByOnlineStatus() throws Exception {
        EventSearchRequest request = new EventSearchRequest();
        request.setIsOnline(false);

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].isOnline").value(false));
    }

    @Test
    void testSearchEvents_EmptyRequest() throws Exception {
        EventSearchRequest request = new EventSearchRequest();

        mockMvc.perform(post("/api/v1/user/events/search")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetMyEvents_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/my-events")
                .header("X-USER_ID", userContextHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("User events retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(testEvent.getId()))
                .andExpect(jsonPath("$.data[0].title").value("Test Event"));
    }

    @Test
    void testGetAllEvents_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/all-events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("All active events retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(testEvent.getId()))
                .andExpect(jsonPath("$.data[0].title").value("Test Event"));
    }

    @Test
    void testGetEventById_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/all-events/{eventId}", testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Event retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(testEvent.getId()))
                .andExpect(jsonPath("$.data.title").value("Test Event"));
    }

    private Event createEventFromRequest(EventRequest request, String username) {
        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setLocation(request.getLocation());
        event.setEventType(request.getEventType());
        event.setIsOnline(request.getIsOnline() != null ? request.getIsOnline() : false);
        event.setMeetingLink(request.getMeetingLink());
        event.setMaxParticipants(request.getMaxParticipants());
        event.setCurrentParticipants(0);
        event.setOrganizerName(request.getOrganizerName());
        event.setOrganizerEmail(request.getOrganizerEmail());
        event.setCreatedByUsername(username);
        event.setActive(true);
        event.setPrivacy(Event.EventPrivacy.PUBLIC);
        event.setInviteMessage(request.getInviteMessage());
        return event;
    }
}
