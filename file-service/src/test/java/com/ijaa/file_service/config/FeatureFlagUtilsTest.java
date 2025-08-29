package com.ijaa.file_service.config;

import com.ijaa.file_service.client.FeatureFlagClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeatureFlagUtilsTest {

    @Mock
    private FeatureFlagClient featureFlagClient;

    private FeatureFlagUtils featureFlagUtils;

    @BeforeEach
    void setUp() {
        featureFlagUtils = new FeatureFlagUtils(featureFlagClient);
    }

    @Test
    void isFeatureEnabled_WhenFeatureIsEnabled_ReturnsTrue() {
        // Given
        String featureName = "file-upload.profile-photo";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(featureName, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isFeatureEnabled(featureName);

        // Then
        assertTrue(result);
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void isFeatureEnabled_WhenFeatureIsDisabled_ReturnsFalse() {
        // Given
        String featureName = "file-upload.profile-photo";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(featureName, false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isFeatureEnabled(featureName);

        // Then
        assertFalse(result);
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void isFeatureEnabled_WhenClientThrowsException_ReturnsTrue() {
        // Given
        String featureName = "file-upload.profile-photo";
        when(featureFlagClient.checkFeatureFlag(featureName)).thenThrow(new RuntimeException("Service unavailable"));

        // When
        boolean result = featureFlagUtils.isFeatureEnabled(featureName);

        // Then
        assertTrue(result); // Default to enabled on error
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void isFeatureEnabled_WhenResponseIsNull_ReturnsTrue() {
        // Given
        String featureName = "file-upload.profile-photo";
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(null);

        // When
        boolean result = featureFlagUtils.isFeatureEnabled(featureName);

        // Then
        assertTrue(result); // Default to enabled on error
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void isFeatureEnabled_WhenResponseDataIsNull_ReturnsTrue() {
        // Given
        String featureName = "file-upload.profile-photo";
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", null);
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isFeatureEnabled(featureName);

        // Then
        assertTrue(result); // Default to enabled on error
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void isFileUploadEnabled_WhenFeatureIsEnabled_ReturnsTrue() {
        // Given
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(FeatureFlagUtils.FILE_UPLOAD, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(FeatureFlagUtils.FILE_UPLOAD)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isFileUploadEnabled();

        // Then
        assertTrue(result);
        verify(featureFlagClient).checkFeatureFlag(FeatureFlagUtils.FILE_UPLOAD);
    }

    @Test
    void isProfilePhotoUploadEnabled_WhenFeatureIsEnabled_ReturnsTrue() {
        // Given
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(FeatureFlagUtils.FILE_UPLOAD_PROFILE_PHOTO, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(FeatureFlagUtils.FILE_UPLOAD_PROFILE_PHOTO)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isProfilePhotoUploadEnabled();

        // Then
        assertTrue(result);
        verify(featureFlagClient).checkFeatureFlag(FeatureFlagUtils.FILE_UPLOAD_PROFILE_PHOTO);
    }

    @Test
    void isCoverPhotoUploadEnabled_WhenFeatureIsEnabled_ReturnsTrue() {
        // Given
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(FeatureFlagUtils.FILE_UPLOAD_COVER_PHOTO, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(FeatureFlagUtils.FILE_UPLOAD_COVER_PHOTO)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isCoverPhotoUploadEnabled();

        // Then
        assertTrue(result);
        verify(featureFlagClient).checkFeatureFlag(FeatureFlagUtils.FILE_UPLOAD_COVER_PHOTO);
    }

    @Test
    void isFileDownloadEnabled_WhenFeatureIsEnabled_ReturnsTrue() {
        // Given
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(FeatureFlagUtils.FILE_DOWNLOAD, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(FeatureFlagUtils.FILE_DOWNLOAD)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isFileDownloadEnabled();

        // Then
        assertTrue(result);
        verify(featureFlagClient).checkFeatureFlag(FeatureFlagUtils.FILE_DOWNLOAD);
    }

    @Test
    void isFileDeleteEnabled_WhenFeatureIsEnabled_ReturnsTrue() {
        // Given
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(FeatureFlagUtils.FILE_DELETE, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(FeatureFlagUtils.FILE_DELETE)).thenReturn(response);

        // When
        boolean result = featureFlagUtils.isFileDeleteEnabled();

        // Then
        assertTrue(result);
        verify(featureFlagClient).checkFeatureFlag(FeatureFlagUtils.FILE_DELETE);
    }

    @Test
    void logFeatureUsage_WhenFeatureIsEnabled_LogsUsage() {
        // Given
        String featureName = "file-upload.profile-photo";
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(featureName, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(response);

        // When
        featureFlagUtils.logFeatureUsage(featureName, userId);

        // Then
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void logFeatureUsage_WhenFeatureIsDisabled_DoesNotLogUsage() {
        // Given
        String featureName = "file-upload.profile-photo";
        String userId = "test-user-id";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(featureName, false);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(response);

        // When
        featureFlagUtils.logFeatureUsage(featureName, userId);

        // Then
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }

    @Test
    void logFeatureUsage_WithNullUserId_HandlesGracefully() {
        // Given
        String featureName = "file-upload.profile-photo";
        FeatureFlagClient.FeatureFlagStatus status = new FeatureFlagClient.FeatureFlagStatus(featureName, true);
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> response = 
            new FeatureFlagClient.ApiResponse<>("Success", "200", status);
        when(featureFlagClient.checkFeatureFlag(featureName)).thenReturn(response);

        // When
        featureFlagUtils.logFeatureUsage(featureName, null);

        // Then
        verify(featureFlagClient).checkFeatureFlag(featureName);
    }
}
