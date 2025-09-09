package com.ijaa.file.controller;

import com.ijaa.file.client.FeatureFlagClient;
import com.ijaa.file.common.exceptions.FeatureDisabledException;
import com.ijaa.file.config.FeatureFlagUtils;
import com.ijaa.file.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class FeatureFlagAopIntegrationTest {

    @Autowired
    private FileController fileController;

    @MockBean
    private FileService fileService;

    @MockBean
    private FeatureFlagClient featureFlagClient;

    @Autowired
    private FeatureFlagUtils featureFlagUtils;

    @Test
    void uploadProfilePhoto_WhenFeatureFlagIsEnabled_AllowsUpload() {
        // Given
        String userId = "test-user-id";
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-upload.profile-photo", true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-upload.profile-photo")).thenReturn(response);

        // When
        ResponseEntity<?> result = fileController.uploadProfilePhoto(userId, file);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.profile-photo");
    }

    @Test
    void uploadProfilePhoto_WhenFeatureFlagIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String userId = "test-user-id";
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-upload.profile-photo", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-upload.profile-photo")).thenReturn(response);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            fileController.uploadProfilePhoto(userId, file);
        });

        assertEquals("Feature 'file-upload.profile-photo' is disabled", exception.getMessage());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.profile-photo");
    }

    @Test
    void uploadCoverPhoto_WhenFeatureFlagIsEnabled_AllowsUpload() {
        // Given
        String userId = "test-user-id";
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-upload.cover-photo", true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-upload.cover-photo")).thenReturn(response);

        // When
        ResponseEntity<?> result = fileController.uploadCoverPhoto(userId, file);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.cover-photo");
    }

    @Test
    void uploadCoverPhoto_WhenFeatureFlagIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String userId = "test-user-id";
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );

        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-upload.cover-photo", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-upload.cover-photo")).thenReturn(response);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            fileController.uploadCoverPhoto(userId, file);
        });

        assertEquals("Feature 'file-upload.cover-photo' is disabled", exception.getMessage());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.cover-photo");
    }

    @Test
    void getProfilePhotoUrl_WhenFeatureFlagIsEnabled_AllowsAccess() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-download", true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-download")).thenReturn(response);

        // When
        ResponseEntity<?> result = fileController.getProfilePhotoUrl(userId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-download");
    }

    @Test
    void getProfilePhotoUrl_WhenFeatureFlagIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-download", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-download")).thenReturn(response);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            fileController.getProfilePhotoUrl(userId);
        });

        assertEquals("Feature 'file-download' is disabled", exception.getMessage());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-download");
    }

    @Test
    void getCoverPhotoUrl_WhenFeatureFlagIsEnabled_AllowsAccess() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-download", true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-download")).thenReturn(response);

        // When
        ResponseEntity<?> result = fileController.getCoverPhotoUrl(userId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-download");
    }

    @Test
    void getCoverPhotoUrl_WhenFeatureFlagIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-download", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-download")).thenReturn(response);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            fileController.getCoverPhotoUrl(userId);
        });

        assertEquals("Feature 'file-download' is disabled", exception.getMessage());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-download");
    }

    @Test
    void deleteProfilePhoto_WhenFeatureFlagIsEnabled_AllowsDeletion() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-delete", true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-delete")).thenReturn(response);

        // When
        ResponseEntity<?> result = fileController.deleteProfilePhoto(userId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-delete");
    }

    @Test
    void deleteProfilePhoto_WhenFeatureFlagIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-delete", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-delete")).thenReturn(response);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            fileController.deleteProfilePhoto(userId);
        });

        assertEquals("Feature 'file-delete' is disabled", exception.getMessage());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-delete");
    }

    @Test
    void deleteCoverPhoto_WhenFeatureFlagIsEnabled_AllowsDeletion() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-delete", true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-delete")).thenReturn(response);

        // When
        ResponseEntity<?> result = fileController.deleteCoverPhoto(userId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-delete");
    }

    @Test
    void deleteCoverPhoto_WhenFeatureFlagIsDisabled_ThrowsFeatureDisabledException() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-delete", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-delete")).thenReturn(response);

        // When & Then
        FeatureDisabledException exception = assertThrows(FeatureDisabledException.class, () -> {
            fileController.deleteCoverPhoto(userId);
        });

        assertEquals("Feature 'file-delete' is disabled", exception.getMessage());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-delete");
    }
}
