package com.ijaa.user.common.exceptions;

public class AdminAlreadyExistsException extends RuntimeException {
    
    public AdminAlreadyExistsException(String message) {
        super(message);
    }
    
    public AdminAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
} 