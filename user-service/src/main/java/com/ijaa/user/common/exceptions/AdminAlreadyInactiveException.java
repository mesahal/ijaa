package com.ijaa.user.common.exceptions;

public class AdminAlreadyInactiveException extends RuntimeException {
    
    public AdminAlreadyInactiveException(String message) {
        super(message);
    }
    
    public AdminAlreadyInactiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
