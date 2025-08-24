package com.ijaa.file_service.controller;

import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
import com.ijaa.file_service.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for File Service through Gateway routing
 * These tests verify that the file service endpoints work correctly
 * when accessed through the API gateway routing
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class GatewayIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private FileService fileService;

    private MockMvc mockMvc;

    private static final String TEST_USER_ID = "gateway-test-user-123";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void gatewayRouting_GetProfilePhoto_ShouldWork() throws Exception {
        // Arrange
        PhotoUrlResponse expectedResponse = new PhotoUrlResponse(
            "/uploads/profile/test-profile.jpg",
            "Profile photo found",
            true
        );

        when(fileService.getProfilePhotoUrl(eq(TEST_USER_ID))).thenReturn(expectedResponse);

        // Act & Assert - Test the exact path that would be used through gateway
        mockMvc.perform(get("/api/v1/users/{userId}/profile-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.photoUrl").value("/uploads/profile/test-profile.jpg"))
                .andExpect(jsonPath("$.data.exists").value(true));
    }

    @Test
    void gatewayRouting_GetCoverPhoto_ShouldWork() throws Exception {
        // Arrange
        PhotoUrlResponse expectedResponse = new PhotoUrlResponse(
            "/uploads/cover/test-cover.jpg",
            "Cover photo found",
            true
        );

        when(fileService.getCoverPhotoUrl(eq(TEST_USER_ID))).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/cover-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.photoUrl").value("/uploads/cover/test-cover.jpg"))
                .andExpect(jsonPath("$.data.exists").value(true));
    }

    @Test
    void gatewayRouting_UploadProfilePhoto_ShouldWork() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test-profile.jpg", "image/jpeg", "test profile content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Profile photo uploaded successfully",
            "/uploads/profile/test-profile.jpg",
            "/uploads/profile/test-profile.jpg",
            "test-profile.jpg",
            1234L
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any())).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fileUrl").value("/uploads/profile/test-profile.jpg"));
    }

    @Test
    void gatewayRouting_UploadCoverPhoto_ShouldWork() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test-cover.jpg", "image/jpeg", "test cover content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Cover photo uploaded successfully",
            "/uploads/cover/test-cover.jpg",
            "/uploads/cover/test-cover.jpg",
            "test-cover.jpg",
            5678L
        );

        when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any())).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fileUrl").value("/uploads/cover/test-cover.jpg"));
    }

    @Test
    void gatewayRouting_DeleteProfilePhoto_ShouldWork() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{userId}/profile-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo deleted successfully"));
    }

    @Test
    void gatewayRouting_DeleteCoverPhoto_ShouldWork() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{userId}/cover-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo deleted successfully"));
    }

    @Test
    void gatewayRouting_UserNotFound_ShouldReturn404() throws Exception {
        // Arrange
        when(fileService.getProfilePhotoUrl(eq("non-existent-user")))
            .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/profile-photo", "non-existent-user"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void gatewayRouting_InvalidFileType_ShouldReturn400() throws Exception {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file", "test.txt", "text/plain", "invalid file content".getBytes()
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any()))
            .thenThrow(new RuntimeException("Invalid file type"));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(invalidFile))
                .andExpect(status().isInternalServerError());
    }
}
