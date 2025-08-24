package com.ijaa.file_service.exceptions;

public class InvalidFileTypeException extends RuntimeException {
    
    public InvalidFileTypeException(String message) {
        super(message);
    }
    
    public InvalidFileTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
