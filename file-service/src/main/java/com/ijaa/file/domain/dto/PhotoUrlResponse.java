package com.ijaa.file.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhotoUrlResponse {
    private String photoUrl;
    private String message;
    private boolean exists;
    
    // Constructor for all fields
    public PhotoUrlResponse(String photoUrl, String message, boolean exists) {
        this.photoUrl = photoUrl;
        this.message = message;
        this.exists = exists;
    }
    
    // Constructor for URL and file type
    public PhotoUrlResponse(String photoUrl, String fileType) {
        this.photoUrl = photoUrl;
        this.message = "File retrieved successfully";
        this.exists = true;
    }
}
