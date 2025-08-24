package com.ijaa.user.common.exceptions;

public class InsufficientPrivilegesException extends RuntimeException {
    
    public InsufficientPrivilegesException(String message) {
        super(message);
    }
    
    public InsufficientPrivilegesException(String message, Throwable cause) {
        super(message, cause);
    }
} 