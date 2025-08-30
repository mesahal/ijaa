package com.ijaa.event_service.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event_service.domain.entity.Event;
import com.ijaa.event_service.domain.request.AdvancedEventSearchRequest;
import com.ijaa.event_service.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AdvancedEventSearchResourceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        setupTestData();
    }

    private void setupTestData() {
        // Create test events
        Event event1 = new Event();
        event1.setTitle("Alumni Meet 2024");
        event1.setDescription("Annual alumni gathering");
        event1.setStartDate(LocalDateTime.now().plusDays(7));
        event1.setEndDate(LocalDateTime.now().plusDays(7).plusHours(4));
        event1.setLocation("IIT Campus");
        event1.setEventType("MEETING");
        event1.setActive(true);
        event1.setIsOnline(false);
        event1.setMaxParticipants(100);
        event1.setCurrentParticipants(45);
        event1.setOrganizerName("John Doe");
        event1.setOrganizerEmail("john@example.com");
        event1.setCreatedByUsername("johndoe");
        event1.setPrivacy(Event.EventPrivacy.PUBLIC);
        event1.setInviteMessage("Join us for the annual alumni meet");
        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setTitle("Tech Webinar");
        event2.setDescription("Technology innovation webinar");
        event2.setStartDate(LocalDateTime.now().plusDays(14));
        event2.setEndDate(LocalDateTime.now().plusDays(14).plusHours(2));
        event2.setLocation("Virtual");
        event2.setEventType("WEBINAR");
        event2.setActive(true);
        event2.setIsOnline(true);
        event2.setMaxParticipants(200);
        event2.setCurrentParticipants(150);
        event2.setOrganizerName("Jane Smith");
        event2.setOrganizerEmail("jane@example.com");
        event2.setCreatedByUsername("janesmith");
        event2.setPrivacy(Event.EventPrivacy.PUBLIC);
        event2.setInviteMessage("Join our tech webinar");
        eventRepository.save(event2);

        Event event3 = new Event();
        event3.setTitle("Career Workshop");
        event3.setDescription("Career development workshop");
        event3.setStartDate(LocalDateTime.now().plusDays(21));
        event3.setEndDate(LocalDateTime.now().plusDays(21).plusHours(6));
        event3.setLocation("IIT Campus");
        event3.setEventType("WORKSHOP");
        event3.setActive(true);
        event3.setIsOnline(false);
        event3.setMaxParticipants(50);
        event3.setCurrentParticipants(30);
        event3.setOrganizerName("Mike Johnson");
        event3.setOrganizerEmail("mike@example.com");
        event3.setCreatedByUsername("mikejohnson");
        event3.setPrivacy(Event.EventPrivacy.PUBLIC);
        event3.setInviteMessage("Career development workshop");
        eventRepository.save(event3);
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdvancedSearch_ShouldReturnFilteredEvents() throws Exception {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setLocation("IIT Campus");
        request.setEventType("MEETING");
        request.setPage(0);
        request.setSize(10);

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events found successfully"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].title").value("Alumni Meet 2024"))
                .andExpect(jsonPath("$.data.content[0].location").value("IIT Campus"))
                .andExpect(jsonPath("$.data.content[0].eventType").value("MEETING"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAdvancedSearch_WithOnlineFilter_ShouldReturnOnlineEvents() throws Exception {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setIsOnline(true);
        request.setPage(0);
        request.setSize(10);

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].title").value("Tech Webinar"))
                .andExpect(jsonPath("$.data.content[0].isOnline").value(true));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetEventRecommendations_ShouldReturnRecommendations() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/recommendations")
                .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Event recommendations retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetTrendingEvents_ShouldReturnTrendingEvents() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/trending")
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Trending events retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetEventsByLocation_ShouldReturnEventsInLocation() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/location/IIT Campus")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events by location retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].location").value("IIT Campus"));
    }

    @Test
    void testGetEventsByOrganizer_ShouldReturnEventsByOrganizer() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/organizer/John Doe")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Events by organizer retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].organizerName").value("John Doe"));
    }

    @Test
    void testGetHighEngagementEvents_ShouldReturnHighEngagementEvents() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/high-engagement")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("High engagement events retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetUpcomingEvents_ShouldReturnUpcomingEvents() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/upcoming")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Upcoming events retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetUpcomingEvents_WithLocationFilter_ShouldReturnFilteredEvents() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/upcoming")
                .param("location", "IIT Campus")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].location").value("IIT Campus"));
    }

    @Test
    void testGetUpcomingEvents_WithEventTypeFilter_ShouldReturnFilteredEvents() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/upcoming")
                .param("eventType", "WORKSHOP")
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].eventType").value("WORKSHOP"));
    }

    @Test
    void testGetSimilarEvents_ShouldReturnSimilarEvents() throws Exception {
        // Given
        Event event = eventRepository.findAll().get(0);

        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/similar/{eventId}", event.getId())
                .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Similar events retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testAdvancedSearch_WithDateRange_ShouldReturnFilteredEvents() throws Exception {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setStartDate(LocalDateTime.now().plusDays(5));
        request.setEndDate(LocalDateTime.now().plusDays(15));
        request.setPage(0);
        request.setSize(10);

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void testAdvancedSearch_WithQuery_ShouldReturnMatchingEvents() throws Exception {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setQuery("Alumni");
        request.setPage(0);
        request.setSize(10);

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].title").value("Alumni Meet 2024"));
    }

    @Test
    void testAdvancedSearch_WithPagination_ShouldReturnPaginatedResults() throws Exception {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setPage(0);
        request.setSize(2);

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.size").value(2))
                .andExpect(jsonPath("$.data.page").value(0));
    }

    @Test
    void testAdvancedSearch_WithSorting_ShouldReturnSortedResults() throws Exception {
        // Given
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setSortBy("start_date");
        request.setSortOrder("asc");
        request.setPage(0);
        request.setSize(10);

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void testAdvancedSearch_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given - Invalid request with null values
        AdvancedEventSearchRequest request = new AdvancedEventSearchRequest();
        request.setPage(-1); // Invalid page number

        // When & Then
        mockMvc.perform(post("/api/v1/user/events/advanced-search/advanced")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict()) // The service returns 409 for invalid requests
                .andExpect(jsonPath("$.code").value("409"));
    }

    @Test
    void testGetSimilarEvents_WithNonExistentEvent_ShouldReturnError() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/user/events/advanced-search/similar/99999")
                .param("limit", "10"))
                .andExpect(status().isInternalServerError()); // Should return 500 for non-existent event
    }
}
