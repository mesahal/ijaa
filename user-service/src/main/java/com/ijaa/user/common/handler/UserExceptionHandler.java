package com.ijaa.user.common.handler;

import com.ijaa.user.common.exceptions.AuthenticationFailedException;
import com.ijaa.user.common.exceptions.PasswordChangeException;
import com.ijaa.user.common.exceptions.UserAlreadyExistsException;
import com.ijaa.user.common.exceptions.UserNotFoundException;
import com.ijaa.user.common.exceptions.FeatureDisabledException;
import com.ijaa.user.domain.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleUserExists(
            UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse<>(ex.getMessage(), "409", null)
        );
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse<?>> handleAuthFailure(
            AuthenticationFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiResponse<>(ex.getMessage(), "401", null)
        );
    }

    @ExceptionHandler(FeatureDisabledException.class)
    public ResponseEntity<ApiResponse<?>> handleFeatureDisabled(
            FeatureDisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiResponse<>(ex.getMessage(), "403", null)
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(
            MethodArgumentNotValidException ex) {
        String error = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Invalid request");
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(error, "400", null)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse<>(ex.getMessage(), "409", null)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(ex.getMessage(), "404", null)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                new ApiResponse<>("Malformed JSON or empty request body", "400", null)
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(ex.getMessage(), "404", null)
        );
    }

    @ExceptionHandler(PasswordChangeException.class)
    public ResponseEntity<ApiResponse<?>> handlePasswordChangeException(PasswordChangeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse<>(ex.getMessage(), "400", null)
        );
    }
}
