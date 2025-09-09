package com.ijaa.file.common.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FeatureDisabledExceptionTest {

    @Test
    void constructor_WithFeatureNameAndMessage_SetsPropertiesCorrectly() {
        // Given
        String featureName = "file-upload.profile-photo";
        String message = "Custom error message";

        // When
        FeatureDisabledException exception = new FeatureDisabledException(featureName, message);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
    }

    @Test
    void constructor_WithFeatureNameOnly_SetsDefaultMessage() {
        // Given
        String featureName = "file-upload.profile-photo";

        // When
        FeatureDisabledException exception = new FeatureDisabledException(featureName);

        // Then
        assertEquals("Feature 'file-upload.profile-photo' is disabled", exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
    }

    @Test
    void constructor_WithNullFeatureName_HandlesGracefully() {
        // Given
        String message = "Custom error message";

        // When
        FeatureDisabledException exception = new FeatureDisabledException(null, message);

        // Then
        assertEquals(message, exception.getMessage());
        assertNull(exception.getFeatureName());
    }

    @Test
    void constructor_WithNullMessage_HandlesGracefully() {
        // Given
        String featureName = "file-upload.profile-photo";

        // When
        FeatureDisabledException exception = new FeatureDisabledException(featureName, null);

        // Then
        assertNull(exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
    }

    @Test
    void constructor_WithEmptyMessage_HandlesGracefully() {
        // Given
        String featureName = "file-upload.profile-photo";
        String message = "";

        // When
        FeatureDisabledException exception = new FeatureDisabledException(featureName, message);

        // Then
        assertEquals("", exception.getMessage());
        assertEquals(featureName, exception.getFeatureName());
    }
}
