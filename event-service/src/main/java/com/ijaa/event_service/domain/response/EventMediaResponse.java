package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventMediaResponse {

    private Long id;
    private Long eventId;
    private String uploadedByUsername;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private String caption;
    private String mediaType;
    private Boolean isApproved;
    private Integer likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 