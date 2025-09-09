package com.ijaa.event.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.entity.EventComment;
import com.ijaa.event.domain.request.EventCommentRequest;
import com.ijaa.event.domain.request.EventRequest;
import com.ijaa.event.repository.EventCommentRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.EventCommentService;
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
public class EventCommentResourceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCommentRepository eventCommentRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventCommentService eventCommentService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Event testEvent;
    private EventComment testComment;
    private String testUsername = "testuser";
    private String userContextHeader;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

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

        // Create test comment
        EventComment comment = new EventComment();
        comment.setEventId(testEvent.getId());
        comment.setUsername(testUsername);
        comment.setContent("Test comment content");
        comment.setParentCommentId(null);
        comment.setLikes(0);
        comment.setReplies(0);
        comment.setIsEdited(false);
        comment.setIsDeleted(false);
        testComment = eventCommentRepository.save(comment);

        // Create user context header
        String userContextJson = String.format(
            "{\"username\":\"%s\",\"userType\":\"USER\",\"role\":\"USER\"}", 
            testUsername
        );
        userContextHeader = Base64.getUrlEncoder().encodeToString(userContextJson.getBytes());
    }

    @Test
    void testCreateComment_Success() throws Exception {
        EventCommentRequest request = new EventCommentRequest();
        request.setEventId(testEvent.getId());
        request.setContent("New test comment");

        mockMvc.perform(post("/api/v1/user/events/comments")
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.message").value("Comment created successfully"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.eventId").value(testEvent.getId()))
                .andExpect(jsonPath("$.data.username").value(testUsername))
                .andExpect(jsonPath("$.data.authorName").value(testUsername))
                .andExpect(jsonPath("$.data.content").value("New test comment"));
    }

    @Test
    void testCreateComment_WithoutAuthentication() throws Exception {
        EventCommentRequest request = new EventCommentRequest();
        request.setEventId(testEvent.getId());
        request.setContent("New test comment");

        mockMvc.perform(post("/api/v1/user/events/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("401"))
                .andExpect(jsonPath("$.message").value("Authentication required"));
    }

    @Test
    void testGetEventComments_WithNestedReplies() throws Exception {
        // Create a reply to the test comment
        EventComment reply = new EventComment();
        reply.setEventId(testEvent.getId());
        reply.setUsername("replyuser");
        reply.setContent("This is a reply");
        reply.setParentCommentId(testComment.getId());
        reply.setLikes(0);
        reply.setReplies(0);
        reply.setIsEdited(false);
        reply.setIsDeleted(false);
        eventCommentRepository.save(reply);

        // Update the parent comment's reply count
        testComment.setReplies(1);
        eventCommentRepository.save(testComment);

        mockMvc.perform(get("/api/v1/user/events/comments/event/{eventId}", testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Event comments retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].id").value(testComment.getId()))
                .andExpect(jsonPath("$.data[0].username").value(testUsername))
                .andExpect(jsonPath("$.data[0].authorName").value(testUsername))
                .andExpect(jsonPath("$.data[0].content").value("Test comment content"))
                .andExpect(jsonPath("$.data[0].replies").isArray())
                .andExpect(jsonPath("$.data[0].replies[0].id").value(reply.getId()))
                .andExpect(jsonPath("$.data[0].replies[0].username").value("replyuser"))
                .andExpect(jsonPath("$.data[0].replies[0].authorName").value("replyuser"))
                .andExpect(jsonPath("$.data[0].replies[0].content").value("This is a reply"));
    }

    @Test
    void testGetPopularComments_WithEventId() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/comments/popular")
                .param("eventId", testEvent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Popular comments retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testGetRecentComments_WithEventId() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/comments/recent")
                .param("eventId", testEvent.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Recent comments retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void testUpdateComment_Success() throws Exception {
        EventCommentRequest request = new EventCommentRequest();
        request.setEventId(testEvent.getId());
        request.setContent("Updated comment content");

        mockMvc.perform(put("/api/v1/user/events/comments/{commentId}", testComment.getId())
                .header("X-USER_ID", userContextHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Comment updated successfully"))
                .andExpect(jsonPath("$.data.content").value("Updated comment content"))
                .andExpect(jsonPath("$.data.isEdited").value(true));
    }

    @Test
    void testDeleteComment_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/user/events/comments/{commentId}", testComment.getId())
                .header("X-USER_ID", userContextHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Comment deleted successfully"));
    }

    @Test
    void testToggleCommentLike_Success() throws Exception {
        mockMvc.perform(post("/api/v1/user/events/comments/{commentId}/like", testComment.getId())
                .header("X-USER_ID", userContextHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Comment like status updated successfully"));
    }

    @Test
    void testGetCommentById_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/comments/{commentId}", testComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("Comment retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(testComment.getId()))
                .andExpect(jsonPath("$.data.username").value(testUsername))
                .andExpect(jsonPath("$.data.authorName").value(testUsername))
                .andExpect(jsonPath("$.data.content").value("Test comment content"));
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
