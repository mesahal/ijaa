package com.ijaa.gateway.handler;

import com.ijaa.gateway.domain.common.ApiResponse;
import com.ijaa.gateway.exceptions.MissingAuthorizationHeaderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GatewayExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public ResponseEntity<ApiResponse<Object>> handleException(MissingAuthorizationHeaderException ex) {
        errorLogger.error(ex.getLocalizedMessage(),ex);
        ApiResponse<Object> apiResponse = buildApiResponse(HttpStatus.UNAUTHORIZED.toString(), ex.getMessage(),null);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }

//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<ApiResponse<Void>> handleException(ExpiredJwtException ex) {
//        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpStatus.UNAUTHORIZED.toString(), ex.getMessage(), null);
//        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        errorLogger.error(ex.getLocalizedMessage(),ex);
        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpStatus.UNAUTHORIZED.toString(), ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
}
