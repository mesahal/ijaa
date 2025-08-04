package com.ijaa.user.common.handler;

import com.ijaa.user.common.exceptions.AdminAlreadyExistsException;
import com.ijaa.user.common.exceptions.AdminNotFoundException;
import com.ijaa.user.common.exceptions.InsufficientPrivilegesException;
import com.ijaa.user.domain.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(AdminAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleAdminAlreadyExistsException(AdminAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(ex.getMessage(), "409", null));
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleAdminNotFoundException(AdminNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ex.getMessage(), "404", null));
    }

    @ExceptionHandler(InsufficientPrivilegesException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficientPrivilegesException(InsufficientPrivilegesException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(ex.getMessage(), "403", null));
    }
} 