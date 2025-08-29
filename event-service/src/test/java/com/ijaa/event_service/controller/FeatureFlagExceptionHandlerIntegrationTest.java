package com.ijaa.event_service.controller;

import com.ijaa.event_service.common.exceptions.FeatureDisabledException;
import com.ijaa.event_service.common.handler.EventExceptionHandler;
import com.ijaa.event_service.domain.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagExceptionHandlerIntegrationTest {

    private final EventExceptionHandler exceptionHandler = new EventExceptionHandler();

    @Test
    void handleFeatureDisabled_ReturnsForbiddenWithProperMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", "Test feature is disabled");

        // When
        ResponseEntity<ApiResponse<Object>> result = exceptionHandler.handleFeatureDisabled(exception);

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
        ResponseEntity<ApiResponse<Object>> result = exceptionHandler.handleFeatureDisabled(exception);

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
        ResponseEntity<ApiResponse<Object>> result = exceptionHandler.handleFeatureDisabled(exception);

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
        ResponseEntity<ApiResponse<Object>> result = exceptionHandler.handleFeatureDisabled(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertEquals("403", result.getBody().getCode());
        assertNull(result.getBody().getData());
    }
}
