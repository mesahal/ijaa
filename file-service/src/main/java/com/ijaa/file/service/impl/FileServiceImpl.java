package com.ijaa.file.service.impl;

import com.ijaa.file.config.FileStorageConfig;
import com.ijaa.file.domain.dto.FileUploadResponse;
import com.ijaa.file.domain.dto.PhotoUrlResponse;
import com.ijaa.file.domain.entity.User;
import com.ijaa.file.domain.entity.EventBanner;
import com.ijaa.file.domain.entity.EventPostMedia;
import com.ijaa.file.exceptions.FileStorageException;
import com.ijaa.file.exceptions.InvalidFileTypeException;
import com.ijaa.file.exceptions.UserNotFoundException;
import com.ijaa.file.repository.UserRepository;
import com.ijaa.file.repository.EventBannerRepository;
import com.ijaa.file.repository.EventPostMediaRepository;
import com.ijaa.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileStorageConfig fileStorageConfig;
    private final UserRepository userRepository;
    private final EventBannerRepository eventBannerRepository;
    private final EventPostMediaRepository eventPostMediaRepository;

    @Override
    public FileUploadResponse uploadProfilePhoto(String userId, MultipartFile file) {
        log.info("Uploading profile photo for user: {}", userId);

        validateFile(file);
        User user = getUserOrCreate(userId);

        try {
            // Create directory if it doesn't exist
            Path uploadDir = Paths.get(fileStorageConfig.getProfilePhotosPath());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Delete old profile photo if exists
            if (user.getProfilePhotoPath() != null) {
                deleteFile(Paths.get(fileStorageConfig.getProfilePhotosPath()).resolve(user.getProfilePhotoPath()));
            }

            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);

            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Store the filename (not the full path) in the database
            user.setProfilePhotoPath(fileName);
            userRepository.save(user);

            String fileUrl = "/ijaa/api/v1/files/users/" + userId + "/profile-photo/file/" + fileName;

            log.info("Profile photo uploaded successfully for user: {}, file: {}", userId, fileName);

            return new FileUploadResponse(
                    "Profile photo uploaded successfully",
                    fileUrl,
                    fileName,
                    file.getSize()
            );

        } catch (IOException e) {
            log.error("Error uploading profile photo for user: {}", userId, e);
            throw new FileStorageException("Failed to upload profile photo", e);
        }
    }

    @Override
    public FileUploadResponse uploadCoverPhoto(String userId, MultipartFile file) {
        log.info("Uploading cover photo for user: {}", userId);

        validateFile(file);
        User user = getUserOrCreate(userId);

        try {
            // Create directory if it doesn't exist
            Path uploadDir = Paths.get(fileStorageConfig.getCoverPhotosPath());
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Delete old cover photo if exists
            if (user.getCoverPhotoPath() != null) {
                deleteFile(Paths.get(fileStorageConfig.getCoverPhotosPath()).resolve(user.getCoverPhotoPath()));
            }

            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);

            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Store the filename (not the full path) in the database
            user.setCoverPhotoPath(fileName);
            userRepository.save(user);

            String fileUrl = "/ijaa/api/v1/files/users/" + userId + "/cover-photo/file/" + fileName;

            log.info("Cover photo uploaded successfully for user: {}, file: {}", userId, fileName);

            return new FileUploadResponse(
                    "Cover photo uploaded successfully",
                    fileUrl,
                    fileName,
                    file.getSize()
            );

        } catch (IOException e) {
            log.error("Error uploading cover photo for user: {}", userId, e);
            throw new FileStorageException("Failed to upload cover photo", e);
        }
    }

    @Override
    public PhotoUrlResponse getProfilePhotoUrl(String userId) {
        log.info("Getting profile photo URL for user: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getProfilePhotoPath() == null) {
            return new PhotoUrlResponse(null, "No profile photo found", false);
        }

        // Clean up the stored path to ensure it's just a filename
        String fileName = extractFileName(user.getProfilePhotoPath());
        
        // Generate the URL that points to the file endpoint
        String photoUrl = "/ijaa/api/v1/files/users/" + userId + "/profile-photo/file/" + fileName;

        return new PhotoUrlResponse(photoUrl, "Profile photo found", true);
    }

    @Override
    public PhotoUrlResponse getCoverPhotoUrl(String userId) {
        log.info("Getting cover photo URL for user: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getCoverPhotoPath() == null) {
            return new PhotoUrlResponse(null, "No cover photo found", false);
        }

        // Clean up the stored path to ensure it's just a filename
        String fileName = extractFileName(user.getCoverPhotoPath());
        
        // Generate the URL that points to the file endpoint
        String photoUrl = "/ijaa/api/v1/files/users/" + userId + "/cover-photo/file/" + fileName;

        return new PhotoUrlResponse(photoUrl, "Cover photo found", true);
    }

    @Override
    public void deleteProfilePhoto(String userId) {
        log.info("Deleting profile photo for user: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getProfilePhotoPath() != null) {
            try {
                Path filePath = Paths.get(fileStorageConfig.getProfilePhotosPath()).resolve(user.getProfilePhotoPath());
                deleteFile(filePath);
                user.setProfilePhotoPath(null);
                userRepository.save(user);
                log.info("Profile photo deleted successfully for user: {}", userId);
            } catch (Exception e) {
                log.error("Error deleting profile photo for user: {}", userId, e);
                throw new FileStorageException("Failed to delete profile photo", e);
            }
        }
    }

    @Override
    public void deleteCoverPhoto(String userId) {
        log.info("Deleting cover photo for user: {}", userId);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getCoverPhotoPath() != null) {
            try {
                Path filePath = Paths.get(fileStorageConfig.getCoverPhotosPath()).resolve(user.getCoverPhotoPath());
                deleteFile(filePath);
                user.setCoverPhotoPath(null);
                userRepository.save(user);
                log.info("Cover photo deleted successfully for user: {}", userId);
            } catch (Exception e) {
                log.error("Error deleting cover photo for user: {}", userId, e);
                throw new FileStorageException("Failed to delete cover photo", e);
            }
        }
    }

    @Override
    public Resource getProfilePhotoFile(String userId, String fileName) {
        log.info("Getting profile photo file for user: {}, file: {}", userId, fileName);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getProfilePhotoPath() == null) {
            throw new FileStorageException("No profile photo found for user: " + userId);
        }

        // Clean up the stored path to ensure it's just a filename
        String storedFileName = extractFileName(user.getProfilePhotoPath());
        
        // Validate that the requested filename matches the user's stored filename
        if (!fileName.equals(storedFileName)) {
            throw new FileStorageException("File access denied: filename mismatch");
        }

        try {
            Path filePath = Paths.get(fileStorageConfig.getProfilePhotosPath()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("Profile photo file not found: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error getting profile photo file for user: {}, file: {}", userId, fileName, e);
            throw new FileStorageException("Failed to get profile photo file", e);
        }
    }

    @Override
    public Resource getCoverPhotoFile(String userId, String fileName) {
        log.info("Getting cover photo file for user: {}, file: {}", userId, fileName);

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));

        if (user.getCoverPhotoPath() == null) {
            throw new FileStorageException("No cover photo found for user: " + userId);
        }

        // Clean up the stored path to ensure it's just a filename
        String storedFileName = extractFileName(user.getCoverPhotoPath());
        
        // Validate that the requested filename matches the user's stored filename
        if (!fileName.equals(storedFileName)) {
            throw new FileStorageException("File access denied: filename mismatch");
        }

        try {
            Path filePath = Paths.get(fileStorageConfig.getCoverPhotosPath()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("Cover photo file not found: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error getting cover photo file for user: {}, file: {}", userId, fileName, e);
            throw new FileStorageException("Failed to get cover photo file", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("File is empty");
        }

        if (file.getSize() > fileStorageConfig.getMaxFileSizeMb() * 1024 * 1024) {
            throw new InvalidFileTypeException("File size exceeds maximum limit of " +
                    fileStorageConfig.getMaxFileSizeMb() + "MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new InvalidFileTypeException("File name is null");
        }

        String fileExtension = getFileExtension(originalFilename).toLowerCase();
        if (!fileStorageConfig.getAllowedImageTypes().contains(fileExtension)) {
            throw new InvalidFileTypeException("File type not allowed. Allowed types: " +
                    String.join(", ", fileStorageConfig.getAllowedImageTypes()));
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1) : "";
    }

    private String getFileExtensionFromContentType(String contentType) {
        if (contentType == null) return null;
        
        switch (contentType.toLowerCase()) {
            case "image/jpeg":
                return "jpg";
            case "image/jpg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/webp":
                return "webp";
            case "video/mp4":
                return "mp4";
            case "video/avi":
                return "avi";
            case "video/quicktime":
                return "mov";
            case "video/x-ms-wmv":
                return "wmv";
            case "video/x-flv":
                return "flv";
            case "video/webm":
                return "webm";
            default:
                return null;
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId + "." + extension;
    }

    /**
     * Extracts just the filename from a path, handling both full paths and just filenames
     */
    private String extractFileName(String path) {
        if (path == null) {
            return null;
        }
        // If it's already just a filename (no path separators), return as is
        if (!path.contains("/") && !path.contains("\\")) {
            return path;
        }
        // Extract filename from path
        return Paths.get(path).getFileName().toString();
    }

    private User getUserOrCreate(String userId) {
        return userRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUserId(userId);
                    newUser.setUsername(userId); // Using userId as username for now
                    newUser.setPassword(""); // Empty password for file service users
                    newUser.setActive(true);
                    return userRepository.save(newUser);
                });
    }

    private void deleteFile(Path filePath) {
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.debug("File deleted: {}", filePath);
            }
        } catch (IOException e) {
            log.warn("Could not delete file: {}", filePath, e);
        }
    }

    // Event Banner methods
    @Override
    public FileUploadResponse uploadEventBanner(String eventId, MultipartFile file) {
        log.info("Uploading event banner for event: {}", eventId);

        try {
            validateFile(file);
            log.debug("File validation passed for event: {}", eventId);
            // Create directory if it doesn't exist
            Path uploadDir = Paths.get(fileStorageConfig.getEventBannersPath());
            log.debug("Event banner upload directory: {}", uploadDir);
            
            // Check if directory exists and is writable
            if (!Files.exists(uploadDir)) {
                log.debug("Creating event banner upload directory: {}", uploadDir);
                Files.createDirectories(uploadDir);
                log.debug("Created event banner upload directory: {}", uploadDir);
            }
            
            // Verify directory is writable
            if (!Files.isWritable(uploadDir)) {
                throw new FileStorageException("Upload directory is not writable: " + uploadDir);
            }
            
            log.debug("Event banner upload directory is ready: {}", uploadDir);

            // Delete old banner if exists
            log.debug("Checking for existing banner for event: {}", eventId);
            EventBanner existingBanner = eventBannerRepository.findByEventId(eventId).orElse(null);
            if (existingBanner != null) {
                log.debug("Found existing banner, deleting old file: {}", existingBanner.getFileName());
                deleteFile(Paths.get(fileStorageConfig.getEventBannersPath()).resolve(existingBanner.getFileName()));
            } else {
                log.debug("No existing banner found for event: {}", eventId);
            }

            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);

            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Store the banner info in the database
            log.debug("Saving banner info to database for event: {}", eventId);
            EventBanner banner = existingBanner != null ? existingBanner : new EventBanner();
            banner.setEventId(eventId);
            banner.setFileName(fileName);
            banner.setFileSize(file.getSize());
            banner.setFileType(file.getContentType());
            
            // Temporarily removing timestamp setting to test basic functionality
            // LocalDateTime now = LocalDateTime.now();
            // if (existingBanner == null) {
            //     banner.setCreatedAt(now);
            // }
            // banner.setUpdatedAt(now);
            
            EventBanner savedBanner = eventBannerRepository.save(banner);
            log.debug("Banner saved to database with ID: {}", savedBanner.getId());

            String fileUrl = "/ijaa/api/v1/files/events/" + eventId + "/banner/file/" + fileName;

            log.info("Event banner uploaded successfully for event: {}, file: {}", eventId, fileName);

            return new FileUploadResponse(
                    "Event banner uploaded successfully",
                    fileUrl,
                    fileName,
                    file.getSize()
            );

        } catch (IOException e) {
            log.error("IO Error uploading event banner for event: {} - Error: {}", eventId, e.getMessage(), e);
            throw new FileStorageException("Failed to upload event banner: IO Error", e);
        } catch (Exception e) {
            log.error("Unexpected error uploading event banner for event: {} - Error: {} - Type: {}", 
                     eventId, e.getMessage(), e.getClass().getSimpleName(), e);
            throw new FileStorageException("Failed to upload event banner: " + e.getMessage(), e);
        }
    }

    @Override
    public PhotoUrlResponse getEventBannerUrl(String eventId) {
        log.info("Getting event banner URL for event: {}", eventId);

        try {
            log.debug("Querying database for banner with eventId: {}", eventId);
            EventBanner banner = eventBannerRepository.findByEventId(eventId)
                    .orElse(null);

            if (banner == null) {
                log.debug("No banner found for event: {}", eventId);
                return new PhotoUrlResponse(null, "No event banner found", false);
            }

            log.debug("Found banner for event: {}, fileName: {}", eventId, banner.getFileName());
            String fileUrl = "/ijaa/api/v1/files/events/" + eventId + "/banner/file/" + banner.getFileName();

            return new PhotoUrlResponse(fileUrl, "Event banner found", true);
        } catch (Exception e) {
            log.error("Error getting event banner URL for event: {}", eventId, e);
            throw new RuntimeException("Failed to get event banner URL", e);
        }
    }

    @Override
    public Resource getEventBannerFile(String eventId, String fileName) {
        log.info("Getting event banner file for event: {}, file: {}", eventId, fileName);

        EventBanner banner = eventBannerRepository.findByEventId(eventId)
                .orElse(null);

        if (banner == null) {
            throw new FileStorageException("No event banner found for event: " + eventId);
        }

        // Validate that the requested filename matches the stored filename
        if (!fileName.equals(banner.getFileName())) {
            throw new FileStorageException("File access denied: filename mismatch");
        }

        try {
            Path filePath = Paths.get(fileStorageConfig.getEventBannersPath()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("Event banner file not found: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error getting event banner file for event: {}, file: {}", eventId, fileName, e);
            throw new FileStorageException("Failed to get event banner file", e);
        }
    }

    @Override
    public void deleteEventBanner(String eventId) {
        log.info("Deleting event banner for event: {}", eventId);

        EventBanner banner = eventBannerRepository.findByEventId(eventId)
                .orElse(null);

        if (banner != null) {
            try {
                Path filePath = Paths.get(fileStorageConfig.getEventBannersPath()).resolve(banner.getFileName());
                deleteFile(filePath);
                eventBannerRepository.delete(banner);
                log.info("Event banner deleted successfully for event: {}", eventId);
            } catch (Exception e) {
                log.error("Error deleting event banner for event: {}", eventId, e);
                throw new FileStorageException("Failed to delete event banner", e);
            }
        }
    }

    // Event Post Media methods
    @Override
    public FileUploadResponse uploadPostMedia(String postId, MultipartFile file, String mediaType) {
        log.info("Uploading post media for post: {}, type: {}", postId, mediaType);

        try {
            validatePostMediaFile(file, mediaType);
            log.debug("File validation passed for post: {}", postId);

            // Create directory if it doesn't exist
            Path uploadDir = Paths.get(fileStorageConfig.getEventPostMediaPath());
            log.debug("Post media upload directory: {}", uploadDir);
            
            if (!Files.exists(uploadDir)) {
                log.debug("Creating post media upload directory: {}", uploadDir);
                Files.createDirectories(uploadDir);
                log.debug("Created post media upload directory: {}", uploadDir);
            }
            
            if (!Files.isWritable(uploadDir)) {
                throw new FileStorageException("Upload directory is not writable: " + uploadDir);
            }
            
            log.debug("Post media upload directory is ready: {}", uploadDir);

            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);

            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("File saved successfully: {}", filePath);

            // Store the media info in the database
            log.debug("Saving media info to database for post: {}", postId);
            EventPostMedia media = new EventPostMedia();
            media.setPostId(postId);
            media.setFileName(fileName);
            media.setFileSize(file.getSize());
            media.setFileType(file.getContentType());
            media.setMediaType(EventPostMedia.MediaType.valueOf(mediaType.toUpperCase()));
            
            // Set file order (count existing files for this post)
            Long existingCount = eventPostMediaRepository.countByPostId(postId);
            media.setFileOrder(existingCount.intValue());
            
            EventPostMedia savedMedia = eventPostMediaRepository.save(media);
            log.debug("Media saved to database with ID: {}", savedMedia.getId());

            String fileUrl = "/ijaa/api/v1/files/posts/" + postId + "/media/" + fileName;

            log.info("Post media uploaded successfully for post: {}, file: {}", postId, fileName);

            return new FileUploadResponse(
                    "Post media uploaded successfully",
                    fileUrl,
                    fileName,
                    file.getSize()
            );

        } catch (IOException e) {
            log.error("IO Error uploading post media for post: {} - Error: {}", postId, e.getMessage(), e);
            throw new FileStorageException("Failed to upload post media: IO Error", e);
        } catch (Exception e) {
            log.error("Unexpected error uploading post media for post: {} - Error: {} - Type: {}", 
                     postId, e.getMessage(), e.getClass().getSimpleName(), e);
            throw new FileStorageException("Failed to upload post media: " + e.getMessage(), e);
        }
    }

    @Override
    public PhotoUrlResponse getPostMediaUrl(String postId, String fileName) {
        log.info("Getting post media URL for post: {}, file: {}", postId, fileName);

        EventPostMedia media = eventPostMediaRepository.findByPostIdAndFileName(postId, fileName)
                .orElse(null);

        if (media == null) {
            throw new FileStorageException("No post media found for post: " + postId + ", file: " + fileName);
        }

        String fileUrl = "/ijaa/api/v1/files/posts/" + postId + "/media/" + fileName;
        return new PhotoUrlResponse(fileUrl, media.getFileType());
    }

    @Override
    public Resource getPostMediaFile(String postId, String fileName) {
        log.info("Getting post media file for post: {}, file: {}", postId, fileName);

        EventPostMedia media = eventPostMediaRepository.findByPostIdAndFileName(postId, fileName)
                .orElse(null);

        if (media == null) {
            throw new FileStorageException("No post media found for post: " + postId + ", file: " + fileName);
        }

        // Validate that the requested filename matches the stored filename
        if (!fileName.equals(media.getFileName())) {
            throw new FileStorageException("File access denied: filename mismatch");
        }

        try {
            Path filePath = Paths.get(fileStorageConfig.getEventPostMediaPath()).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("Post media file not found: " + fileName);
            }
        } catch (Exception e) {
            log.error("Error getting post media file for post: {}, file: {}", postId, fileName, e);
            throw new FileStorageException("Failed to get post media file", e);
        }
    }

    @Override
    public void deletePostMedia(String postId, String fileName) {
        log.info("Deleting post media for post: {}, file: {}", postId, fileName);

        EventPostMedia media = eventPostMediaRepository.findByPostIdAndFileName(postId, fileName)
                .orElse(null);

        if (media != null) {
            try {
                Path filePath = Paths.get(fileStorageConfig.getEventPostMediaPath()).resolve(fileName);
                deleteFile(filePath);
                eventPostMediaRepository.delete(media);
                log.info("Post media deleted successfully for post: {}, file: {}", postId, fileName);
            } catch (Exception e) {
                log.error("Error deleting post media for post: {}, file: {}", postId, fileName, e);
                throw new FileStorageException("Failed to delete post media", e);
            }
        }
    }

    @Override
    public void deleteAllPostMedia(String postId) {
        log.info("Deleting all post media for post: {}", postId);

        try {
            List<EventPostMedia> mediaList = eventPostMediaRepository.findByPostIdOrderByFileOrderAsc(postId);
            
            for (EventPostMedia media : mediaList) {
                Path filePath = Paths.get(fileStorageConfig.getEventPostMediaPath()).resolve(media.getFileName());
                deleteFile(filePath);
            }
            
            eventPostMediaRepository.deleteByPostId(postId);
            log.info("All post media deleted successfully for post: {}", postId);
        } catch (Exception e) {
            log.error("Error deleting all post media for post: {}", postId, e);
            throw new FileStorageException("Failed to delete all post media", e);
        }
    }

    private void validatePostMediaFile(MultipartFile file, String mediaType) {
        if (file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new FileStorageException("File content type is null");
        }

        // Validate file type based on media type
        if ("IMAGE".equalsIgnoreCase(mediaType)) {
            // Extract file extension from content type or filename
            String fileExtension = getFileExtensionFromContentType(contentType);
            if (fileExtension == null) {
                // Fallback to filename extension
                fileExtension = getFileExtension(file.getOriginalFilename());
            }
            
            if (!fileStorageConfig.getAllowedImageTypes().contains(fileExtension.toLowerCase())) {
                throw new InvalidFileTypeException("Invalid image type: " + contentType + ". Allowed types: " + 
                    String.join(", ", fileStorageConfig.getAllowedImageTypes()));
            }
            
            // Check file size for images
            long maxSize = fileStorageConfig.getMaxFileSizeMb() * 1024 * 1024;
            if (file.getSize() > maxSize) {
                throw new FileStorageException("File size exceeds maximum allowed size: " + fileStorageConfig.getMaxFileSizeMb() + "MB");
            }
        } else if ("VIDEO".equalsIgnoreCase(mediaType)) {
            // Extract file extension from content type or filename
            String fileExtension = getFileExtensionFromContentType(contentType);
            if (fileExtension == null) {
                // Fallback to filename extension
                fileExtension = getFileExtension(file.getOriginalFilename());
            }
            
            if (!fileStorageConfig.getAllowedVideoTypes().contains(fileExtension.toLowerCase())) {
                throw new InvalidFileTypeException("Invalid video type: " + contentType + ". Allowed types: " + 
                    String.join(", ", fileStorageConfig.getAllowedVideoTypes()));
            }
            
            // Check file size for videos
            long maxSize = fileStorageConfig.getMaxVideoSizeMb() * 1024 * 1024;
            if (file.getSize() > maxSize) {
                throw new FileStorageException("File size exceeds maximum allowed size: " + fileStorageConfig.getMaxVideoSizeMb() + "MB");
            }
        } else {
            throw new FileStorageException("Invalid media type: " + mediaType);
        }
    }

    @Override
    public List<com.ijaa.file.domain.dto.PostMediaResponse> getAllPostMedia(String postId) {
        log.info("Getting all post media for post: {}", postId);

        List<EventPostMedia> mediaList = eventPostMediaRepository.findByPostIdOrderByFileOrderAsc(postId);
        
        return mediaList.stream()
                .map(media -> com.ijaa.file.domain.dto.PostMediaResponse.builder()
                        .id(media.getId())
                        .fileName(media.getFileName())
                        .fileUrl("/ijaa/api/v1/files/posts/" + postId + "/media/" + media.getFileName())
                        .fileType(media.getFileType())
                        .mediaType(media.getMediaType().name())
                        .fileSize(media.getFileSize())
                        .fileOrder(media.getFileOrder())
                        .createdAt(media.getCreatedAt())
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }
}
