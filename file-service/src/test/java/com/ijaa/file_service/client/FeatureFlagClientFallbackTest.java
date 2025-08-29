package com.ijaa.file_service.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagClientFallbackTest {

    private final FeatureFlagClientFallback fallback = new FeatureFlagClientFallback();

    @Test
    void checkFeatureFlag_WhenUserServiceUnavailable_ReturnsEnabled() {
        // Given
        String featureName = "file-upload.profile-photo";

        // When
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> result = fallback.checkFeatureFlag(featureName);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isEnabled());
        assertEquals(featureName, result.getData().getName());
    }

    @Test
    void checkFeatureFlag_WithNullFeatureName_ReturnsEnabled() {
        // Given
        String featureName = null;

        // When
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> result = fallback.checkFeatureFlag(featureName);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isEnabled());
        assertEquals(featureName, result.getData().getName());
    }

    @Test
    void checkFeatureFlag_WithEmptyFeatureName_ReturnsEnabled() {
        // Given
        String featureName = "";

        // When
        FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> result = fallback.checkFeatureFlag(featureName);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isEnabled());
        assertEquals(featureName, result.getData().getName());
    }

    @Test
    void checkFeatureFlag_WithDifferentFeatureNames_AllReturnEnabled() {
        // Given
        String[] featureNames = {
                "file-upload.profile-photo",
                "file-upload.cover-photo",
                "file-download",
                "file-delete",
                "user.registration",
                "events.creation"
        };

        // When & Then
        for (String featureName : featureNames) {
            FeatureFlagClient.ApiResponse<FeatureFlagClient.FeatureFlagStatus> result = fallback.checkFeatureFlag(featureName);
            assertNotNull(result, "Response should not be null for feature: " + featureName);
            assertNotNull(result.getData(), "Response data should not be null for feature: " + featureName);
            assertTrue(result.getData().isEnabled(), "Feature flag '" + featureName + "' should default to enabled");
            assertEquals(featureName, result.getData().getName(), "Feature name should match for: " + featureName);
        }
    }
}
