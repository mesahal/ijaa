package com.ijaa.event_service.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMediaRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotBlank(message = "File name is required")
    @Size(max = 200, message = "File name must be less than 200 characters")
    private String fileName;

    @NotBlank(message = "File URL is required")
    @Size(max = 500, message = "File URL must be less than 500 characters")
    private String fileUrl;

    @NotBlank(message = "File type is required")
    @Size(max = 50, message = "File type must be less than 50 characters")
    private String fileType;

    @NotNull(message = "File size is required")
    private Long fileSize;

    @Size(max = 200, message = "Caption must be less than 200 characters")
    private String caption;

    @NotNull(message = "Media type is required")
    private String mediaType; // IMAGE, VIDEO, DOCUMENT
} 