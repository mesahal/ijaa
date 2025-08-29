package com.ijaa.file_service.controller;

import com.ijaa.file_service.client.FeatureFlagClient;
import com.ijaa.file_service.domain.common.ApiResponse;
import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
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
class FeatureFlagExceptionHandlerIntegrationTest {

    @Autowired
    private FileController fileController;

    @MockBean
    private FeatureFlagClient featureFlagClient;

    @Test
    void uploadProfilePhoto_WhenFeatureFlagIsDisabled_ReturnsForbiddenWithProperMessage() {
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

        // When
        ResponseEntity<ApiResponse<FileUploadResponse>> result = fileController.uploadProfilePhoto(userId, file);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-upload.profile-photo' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.profile-photo");
    }

    @Test
    void uploadCoverPhoto_WhenFeatureFlagIsDisabled_ReturnsForbiddenWithProperMessage() {
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

        // When
        ResponseEntity<ApiResponse<FileUploadResponse>> result = fileController.uploadCoverPhoto(userId, file);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-upload.cover-photo' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.cover-photo");
    }

    @Test
    void getProfilePhotoUrl_WhenFeatureFlagIsDisabled_ReturnsForbiddenWithProperMessage() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-download", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-download")).thenReturn(response);

        // When
        ResponseEntity<ApiResponse<PhotoUrlResponse>> result = fileController.getProfilePhotoUrl(userId);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-download' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-download");
    }

    @Test
    void getCoverPhotoUrl_WhenFeatureFlagIsDisabled_ReturnsForbiddenWithProperMessage() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-download", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-download")).thenReturn(response);

        // When
        ResponseEntity<ApiResponse<PhotoUrlResponse>> result = fileController.getCoverPhotoUrl(userId);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-download' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-download");
    }

    @Test
    void deleteProfilePhoto_WhenFeatureFlagIsDisabled_ReturnsForbiddenWithProperMessage() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-delete", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-delete")).thenReturn(response);

        // When
        ResponseEntity<ApiResponse<Void>> result = fileController.deleteProfilePhoto(userId);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-delete' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-delete");
    }

    @Test
    void deleteCoverPhoto_WhenFeatureFlagIsDisabled_ReturnsForbiddenWithProperMessage() {
        // Given
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus("file-delete", false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag("file-delete")).thenReturn(response);

        // When
        ResponseEntity<ApiResponse<Void>> result = fileController.deleteCoverPhoto(userId);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-delete' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-delete");
    }

    @Test
    void uploadProfilePhoto_WhenFeatureFlagIsDisabledWithCustomMessage_ReturnsForbiddenWithCustomMessage() {
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

        // When
        ResponseEntity<ApiResponse<FileUploadResponse>> result = fileController.uploadProfilePhoto(userId, file);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'file-upload.profile-photo' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
        verify(featureFlagClient, atLeastOnce()).checkFeatureFlag("file-upload.profile-photo");
    }
}
