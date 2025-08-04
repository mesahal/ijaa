package com.ijaa.user.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementRequest {
    
    @NotBlank(message = "Announcement title is required")
    private String title;
    
    @NotBlank(message = "Announcement content is required")
    private String content;
    
    private String category;
    
    private Boolean isUrgent = false;
    
    @NotBlank(message = "Author name is required")
    private String authorName;
    
    @Email(message = "Author email must be valid")
    @NotBlank(message = "Author email is required")
    private String authorEmail;
    
    private String imageUrl;
} 