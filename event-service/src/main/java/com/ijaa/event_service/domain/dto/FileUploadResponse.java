package com.ijaa.event_service.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private String message;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
}
