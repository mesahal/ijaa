package com.ijaa.user.common.exceptions;

public class AdminSelfDeactivationException extends RuntimeException {
    
    public AdminSelfDeactivationException(String message) {
        super(message);
    }
    
    public AdminSelfDeactivationException(String message, Throwable cause) {
        super(message, cause);
    }
}
