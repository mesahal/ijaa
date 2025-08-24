package com.ijaa.file_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadResponse {
    private String message;
    private String filePath;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    
    // Constructor for all fields
    public FileUploadResponse(String message, String filePath, String fileUrl, String fileName, Long fileSize) {
        this.message = message;
        this.filePath = filePath;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
