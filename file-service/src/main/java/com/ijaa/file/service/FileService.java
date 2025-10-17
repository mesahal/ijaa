package com.ijaa.file.service;

import com.ijaa.file.domain.dto.FileUploadResponse;
import com.ijaa.file.domain.dto.PhotoUrlResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    FileUploadResponse uploadProfilePhoto(String userId, MultipartFile file);
    
    FileUploadResponse uploadCoverPhoto(String userId, MultipartFile file);
    
    PhotoUrlResponse getProfilePhotoUrl(String userId);
    
    PhotoUrlResponse getCoverPhotoUrl(String userId);
    
    Resource getProfilePhotoFile(String userId, String fileName);
    
    Resource getCoverPhotoFile(String userId, String fileName);
    
    void deleteProfilePhoto(String userId);
    
    void deleteCoverPhoto(String userId);
    
    // Event Banner methods
    FileUploadResponse uploadEventBanner(String eventId, MultipartFile file);
    
    PhotoUrlResponse getEventBannerUrl(String eventId);
    
    Resource getEventBannerFile(String eventId, String fileName);
    
    void deleteEventBanner(String eventId);
    
    // Event Post Media methods
    FileUploadResponse uploadPostMedia(String postId, MultipartFile file, String mediaType);
    
    PhotoUrlResponse getPostMediaUrl(String postId, String fileName);
    
    Resource getPostMediaFile(String postId, String fileName);
    
    void deletePostMedia(String postId, String fileName);
    
    void deleteAllPostMedia(String postId);
    
    java.util.List<com.ijaa.file.domain.dto.PostMediaResponse> getAllPostMedia(String postId);
}
