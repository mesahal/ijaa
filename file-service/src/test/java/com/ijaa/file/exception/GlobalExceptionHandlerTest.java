package com.ijaa.file.exception;

import com.ijaa.file.common.exceptions.FeatureDisabledException;
import com.ijaa.file.domain.common.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleFeatureDisabledException_ReturnsForbiddenWithProperMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", "Test feature is disabled");

        // When
        ResponseEntity<ApiResponse<Void>> result = exceptionHandler.handleFeatureDisabledException(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Test feature is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
    }

    @Test
    void handleFeatureDisabledException_WithDefaultMessage_ReturnsForbiddenWithDefaultMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature");

        // When
        ResponseEntity<ApiResponse<Void>> result = exceptionHandler.handleFeatureDisabledException(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
    }

    @Test
    void handleFeatureDisabledException_WithNullMessage_ReturnsForbiddenWithDefaultMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", null);

        // When
        ResponseEntity<ApiResponse<Void>> result = exceptionHandler.handleFeatureDisabledException(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
    }

    @Test
    void handleFeatureDisabledException_WithEmptyMessage_ReturnsForbiddenWithDefaultMessage() {
        // Given
        FeatureDisabledException exception = new FeatureDisabledException("test-feature", "");

        // When
        ResponseEntity<ApiResponse<Void>> result = exceptionHandler.handleFeatureDisabledException(exception);

        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNotNull(result.getBody());
        assertFalse(result.getBody().isSuccess());
        assertEquals("Feature 'test-feature' is disabled", result.getBody().getMessage());
        assertNull(result.getBody().getData());
    }
}
