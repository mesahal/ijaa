package com.ijaa.user.common.exceptions;

public class UserContextException extends RuntimeException {
    public UserContextException(String message) {
        super(message);
    }

    public UserContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
