package com.ijaa.file_service.exception;

import com.ijaa.file_service.domain.common.ApiResponse;
import com.ijaa.file_service.exceptions.FileStorageException;
import com.ijaa.file_service.exceptions.InvalidFileTypeException;
import com.ijaa.file_service.exceptions.UserNotFoundException;
import com.ijaa.file_service.common.exceptions.FeatureDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidFileTypeException(InvalidFileTypeException e) {
        log.error("Invalid file type exception: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileStorageException(FileStorageException e) {
        log.error("File storage exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFoundException(UserNotFoundException e) {
        log.error("User not found exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(FeatureDisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleFeatureDisabledException(FeatureDisabledException e) {
        log.warn("Feature disabled exception: {} - Feature: {}", e.getMessage(), e.getFeatureName());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("Max upload size exceeded: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ApiResponse.error("File size exceeds the maximum allowed limit"));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        log.error("Missing request part: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ApiResponse.error("File is required. Please provide a file to upload."));
    }

    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingServletRequestParameterException(org.springframework.web.bind.MissingServletRequestParameterException e) {
        log.error("Missing request parameter: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ApiResponse.error("File parameter is required. Please provide a file to upload."));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiResponse<Void>> handleMultipartException(MultipartException e) {
        log.error("Multipart exception: {}", e.getMessage());
        return ResponseEntity.badRequest()
            .body(ApiResponse.error("File is required. Please provide a valid file to upload."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception e) {
        log.error("Unexpected error: {} - Type: {} - Stack trace: {}", 
                 e.getMessage(), e.getClass().getSimpleName(), e.getStackTrace(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error("An unexpected error occurred: " + e.getMessage()));
    }
}
