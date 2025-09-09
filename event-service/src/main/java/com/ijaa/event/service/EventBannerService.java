package com.ijaa.event.service;

import com.ijaa.event.domain.dto.FileUploadResponse;
import com.ijaa.event.domain.dto.PhotoUrlResponse;
import org.springframework.web.multipart.MultipartFile;

public interface EventBannerService {

    // Upload event banner (delegates to File Service)
    FileUploadResponse uploadBanner(String eventId, MultipartFile file, String username);

    // Get event banner URL (delegates to File Service)
    PhotoUrlResponse getBannerUrl(String eventId);

    // Delete event banner (delegates to File Service)
    void deleteBanner(String eventId, String username);
}
