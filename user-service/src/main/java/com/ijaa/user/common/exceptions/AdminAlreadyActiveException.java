package com.ijaa.user.common.exceptions;

public class AdminAlreadyActiveException extends RuntimeException {
    
    public AdminAlreadyActiveException(String message) {
        super(message);
    }
    
    public AdminAlreadyActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
