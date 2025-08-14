package com.ijaa.user.common.exceptions;

public class UserAlreadyUnblockedException extends RuntimeException {
    
    public UserAlreadyUnblockedException(String message) {
        super(message);
    }
    
    public UserAlreadyUnblockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
