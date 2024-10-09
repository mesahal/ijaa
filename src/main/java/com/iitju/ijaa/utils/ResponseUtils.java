package com.iitju.ijaa.utils;

import com.iitju.ijaa.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static <T> ResponseEntity<ApiResponse<T>> createHttpResponse(HttpStatus status,String message, T data) {
        ApiResponse<T> apiResponse = new ApiResponse<>("success", message, data);
        return new ResponseEntity<>(apiResponse, status);
    }
}
