package com.ijaa.file_service.service;

import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    FileUploadResponse uploadProfilePhoto(String userId, MultipartFile file);
    
    FileUploadResponse uploadCoverPhoto(String userId, MultipartFile file);
    
    PhotoUrlResponse getProfilePhotoUrl(String userId);
    
    PhotoUrlResponse getCoverPhotoUrl(String userId);
    
    void deleteProfilePhoto(String userId);
    
    void deleteCoverPhoto(String userId);
}
