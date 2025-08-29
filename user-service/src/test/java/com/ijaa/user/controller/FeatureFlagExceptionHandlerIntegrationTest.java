package com.ijaa.user.controller;

import com.ijaa.user.common.exceptions.FeatureDisabledException;
import com.ijaa.user.common.handler.UserExceptionHandler;
import com.ijaa.user.domain.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagExceptionHandlerIntegrationTest {

    private final UserExceptionHandler exceptionHandler = new UserExceptionHandler();

    @Test
    void handleFeatureDisabled_ReturnsForbiddenWithProperMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", "Test feature is disabled");

        // When
        ResponseEntity<ApiResponse<?>> result = exceptionHandler.handleFeatureDisabled(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Test feature is disabled", result.getBody().getMessage());
        assertEquals("403", result.getBody().getCode());
        assertNull(result.getBody().getData());
    }

    @Test
    void handleFeatureDisabled_WithDefaultMessage_ReturnsForbiddenWithDefaultMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature");

        // When
        ResponseEntity<ApiResponse<?>> result = exceptionHandler.handleFeatureDisabled(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertEquals("403", result.getBody().getCode());
        assertNull(result.getBody().getData());
    }

    @Test
    void handleFeatureDisabled_WithNullMessage_ReturnsForbiddenWithDefaultMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", null);

        // When
        ResponseEntity<ApiResponse<?>> result = exceptionHandler.handleFeatureDisabled(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertEquals("403", result.getBody().getCode());
        assertNull(result.getBody().getData());
    }

    @Test
    void handleFeatureDisabled_WithEmptyMessage_ReturnsForbiddenWithDefaultMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", "");

        // When
        ResponseEntity<ApiResponse<?>> result = exceptionHandler.handleFeatureDisabled(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertEquals("403", result.getBody().getCode());
        assertNull(result.getBody().getData());
    }
}
