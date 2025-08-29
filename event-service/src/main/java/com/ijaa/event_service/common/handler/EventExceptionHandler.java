package com.ijaa.event_service.common.handler;

import com.ijaa.event_service.common.exceptions.FeatureDisabledException;
import com.ijaa.event_service.domain.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(FeatureDisabledException.class)
    public ResponseEntity<ApiResponse<Object>> handleFeatureDisabled(FeatureDisabledException ex) {
        errorLogger.warn("Feature disabled exception: {} - Feature: {}", ex.getMessage(), ex.getFeatureName());
        return new ResponseEntity<>(
            buildApiResponse("403", ex.getMessage(), null), 
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(
            buildApiResponse("409", ex.getMessage(), null), 
            HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntime(RuntimeException ex) {
        errorLogger.error("Runtime exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
            buildApiResponse("500", ex.getMessage(), null), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        errorLogger.error("Unexpected error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(
            buildApiResponse("500", "An unexpected error occurred", null), 
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
