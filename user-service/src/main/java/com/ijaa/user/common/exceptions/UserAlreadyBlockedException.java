package com.ijaa.user.common.exceptions;

public class UserAlreadyBlockedException extends RuntimeException {
    
    public UserAlreadyBlockedException(String message) {
        super(message);
    }
    
    public UserAlreadyBlockedException(String message, Throwable cause) {
        super(message, cause);
    }
}
