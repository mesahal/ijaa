package com.ijaa.file.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileUploadResponse {
    private String message;
    private String fileUrl;
    private String fileName;
    private Long fileSize;
    
    // Constructor for all fields (removed filePath)
    public FileUploadResponse(String message, String fileUrl, String fileName, Long fileSize) {
        this.message = message;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }
}
