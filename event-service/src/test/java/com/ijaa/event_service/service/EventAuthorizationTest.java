package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.request.EventRequest;
import com.ijaa.event_service.domain.request.EventReminderRequest;
import com.ijaa.event_service.domain.request.EventCommentRequest;
import com.ijaa.event_service.service.EventService;
import com.ijaa.event_service.service.EventReminderService;
import com.ijaa.event_service.service.EventCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EventAuthorizationTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventReminderService eventReminderService;

    @Autowired
    private EventCommentService eventCommentService;

    @Test
    @WithMockUser(roles = "USER")
    void testCreateEventWithUserRole_ShouldSucceed() {
        // Given
        EventRequest request = new EventRequest();
        request.setTitle("Test Event");
        request.setDescription("Test Description");
        request.setStartDate(LocalDateTime.parse("2024-12-25T18:00:00"));
        request.setEndDate(LocalDateTime.parse("2024-12-25T22:00:00"));
        request.setMaxParticipants(100);
        request.setOrganizerName("Test User");
        request.setOrganizerEmail("test@example.com");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventService.createEventForUser(request, "testuser");
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUserEventsWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventService.getEventsByUser("testuser");
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetActiveEventsByUserWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventService.getActiveEventsByUser("testuser");
            assertNotNull(result);
        });
    }

    @Test
    void testGetAllActiveEvents_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting all active events are accessible
        assertDoesNotThrow(() -> {
            var result = eventService.getActiveEvents();
            assertNotNull(result);
        });
    }

    @Test
    void testGetEventById_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting event by ID are accessible
        assertDoesNotThrow(() -> {
            var result = eventService.getEventById(1L);
            // This might return null if event doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEventWithUserRole_ShouldSucceed() {
        // Given
        EventRequest request = new EventRequest();
        request.setTitle("Updated Test Event");
        request.setDescription("Updated Test Description");
        request.setStartDate(LocalDateTime.parse("2024-12-25T18:00:00"));
        request.setEndDate(LocalDateTime.parse("2024-12-25T22:00:00"));
        request.setMaxParticipants(100);
        request.setOrganizerName("Test User");
        request.setOrganizerEmail("test@example.com");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventService.updateEventForUser(1L, request, "testuser");
            // This might throw exception if event doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteEventWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            eventService.deleteEventForUser(1L, "testuser");
            // This might throw exception if event doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSearchEventsWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventService.searchEvents("Test Location", "MEETING", null, null, false, "Test User", "Test Event", "Test Description");
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testSetReminderWithUserRole_ShouldSucceed() {
        // Given
        EventReminderRequest request = new EventReminderRequest();
        request.setEventId(1L);
        request.setReminderTime(LocalDateTime.parse("2024-12-25T17:00:00"));
        request.setReminderType("ONE_HOUR_BEFORE");
        request.setCustomMessage("Test reminder");
        request.setChannel("EMAIL");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventReminderService.setReminder(request, "testuser");
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetUserRemindersWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventReminderService.getUserReminders("testuser");
            assertNotNull(result);
        });
    }

    @Test
    void testGetEventReminders_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting event reminders are accessible
        assertDoesNotThrow(() -> {
            var result = eventReminderService.getEventReminders(1L);
            assertNotNull(result);
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateReminderWithUserRole_ShouldSucceed() {
        // Given
        EventReminderRequest request = new EventReminderRequest();
        request.setEventId(1L);
        request.setReminderTime(LocalDateTime.parse("2024-12-25T17:00:00"));
        request.setReminderType("ONE_HOUR_BEFORE");
        request.setCustomMessage("Updated test reminder");
        request.setChannel("EMAIL");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventReminderService.updateReminder(1L, request, "testuser");
            // This might throw exception if reminder doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteReminderWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            eventReminderService.deleteReminder(1L, "testuser");
            // This might throw exception if reminder doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCreateCommentWithUserRole_ShouldSucceed() {
        // Given
        EventCommentRequest request = new EventCommentRequest();
        request.setEventId(1L);
        request.setContent("Test comment");

        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventCommentService.createComment(request, "testuser");
            assertNotNull(result);
        });
    }

    @Test
    void testGetEventComments_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting event comments are accessible
        assertDoesNotThrow(() -> {
            var result = eventCommentService.getEventComments(1L, 0, 20);
            assertNotNull(result);
        });
    }

    @Test
    void testGetAllEventComments_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting all event comments are accessible
        assertDoesNotThrow(() -> {
            var result = eventCommentService.getEventCommentsWithReplies(1L);
            assertNotNull(result);
        });
    }

    @Test
    void testGetCommentById_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting comment by ID are accessible
        assertDoesNotThrow(() -> {
            var result = eventCommentService.getComment(1L);
            // This might return null if comment doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateCommentWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventCommentService.updateComment(1L, "Updated test comment", "testuser");
            // This might throw exception if comment doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteCommentWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            eventCommentService.deleteComment(1L, "testuser");
            // This might throw exception if comment doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    @WithMockUser(roles = "USER")
    void testToggleCommentLikeWithUserRole_ShouldSucceed() {
        // When & Then
        assertDoesNotThrow(() -> {
            var result = eventCommentService.toggleLike(1L, "testuser");
            // This might throw exception if comment doesn't exist, but shouldn't throw security exception
        });
    }

    @Test
    void testGetUserComments_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting user comments are accessible
        assertDoesNotThrow(() -> {
            var result = eventCommentService.getUserComments("testuser", 0, 20);
            assertNotNull(result);
        });
    }

    @Test
    void testGetRecentComments_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting recent comments are accessible
        assertDoesNotThrow(() -> {
            var result = eventCommentService.getRecentComments(0, 20);
            assertNotNull(result);
        });
    }

    @Test
    void testGetPopularComments_ShouldBeAccessibleWithoutAuthentication() {
        // Test that public endpoints like getting popular comments are accessible
        assertDoesNotThrow(() -> {
            var result = eventCommentService.getPopularComments(0, 20);
            assertNotNull(result);
        });
    }
}
