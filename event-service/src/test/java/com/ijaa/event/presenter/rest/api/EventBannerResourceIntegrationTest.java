package com.ijaa.event.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
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
public class EventBannerResourceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Register JavaTimeModule for LocalDateTime serialization
        objectMapper.registerModule(new JavaTimeModule());
        
        // Create a test event
        testEvent = new Event();
        testEvent.setTitle("Test Event");
        testEvent.setDescription("Test Event Description");
        testEvent.setLocation("Test Location");
        testEvent.setStartDate(LocalDateTime.now().plusDays(1));
        testEvent.setEndDate(LocalDateTime.now().plusDays(2));
        testEvent.setOrganizerUsername("testuser");
        testEvent.setIsActive(true);
        testEvent = eventRepository.save(testEvent);
    }

    @Test
    void testUploadBanner_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-banner.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/user/events/banner/" + testEvent.getId())
                .file(file)
                .header("X-USER_ID", "testuser")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Banner uploaded successfully"))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.data.message").value("Event banner uploaded successfully"))
                .andExpect(jsonPath("$.data.fileName").exists())
                .andExpect(jsonPath("$.data.fileSize").exists());
    }

    @Test
    void testUploadBanner_WithoutAuthentication() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-banner.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/user/events/banner/" + testEvent.getId())
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"));
    }

    @Test
    void testUploadBanner_EmptyFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-banner.jpg",
            "image/jpeg",
            new byte[0]
        );

        mockMvc.perform(multipart("/api/v1/user/events/banner/" + testEvent.getId())
                .file(file)
                .header("X-USER_ID", "testuser")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("File is required. Please provide a valid image file."));
    }

    @Test
    void testGetBannerUrl_Success() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/banner/" + testEvent.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Banner URL retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    void testGetBannerUrl_EventNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/user/events/banner/999999"))
                .andExpect(status().isOk()) // Should return success with exists: false
                .andExpect(jsonPath("$.message").value("Banner URL retrieved successfully"))
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    void testDeleteBanner_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/user/events/banner/" + testEvent.getId())
                .header("X-USER_ID", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Banner deleted successfully"))
                .andExpect(jsonPath("$.code").value("200"));
    }

    @Test
    void testDeleteBanner_WithoutAuthentication() throws Exception {
        mockMvc.perform(delete("/api/v1/user/events/banner/" + testEvent.getId()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"));
    }

    @Test
    void testDeleteBanner_EventNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/user/events/banner/999999")
                .header("X-USER_ID", "testuser"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Failed to delete banner: Event not found"));
    }
}
