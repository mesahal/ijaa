package com.ijaa.file.service.impl;

import com.ijaa.file.config.FileStorageConfig;
import com.ijaa.file.domain.dto.FileUploadResponse;
import com.ijaa.file.domain.dto.PhotoUrlResponse;
import com.ijaa.file.domain.entity.User;
import com.ijaa.file.domain.entity.EventBanner;
import com.ijaa.file.exceptions.FileStorageException;
import com.ijaa.file.exceptions.InvalidFileTypeException;
import com.ijaa.file.exceptions.UserNotFoundException;
import com.ijaa.file.repository.UserRepository;
import com.ijaa.file.repository.EventBannerRepository;
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

            String fileUrl = "/ijaa/api/v1/file/users/" + userId + "/profile-photo/file/" + fileName;

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

            String fileUrl = "/ijaa/api/v1/file/users/" + userId + "/cover-photo/file/" + fileName;

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
        String photoUrl = "/ijaa/api/v1/file/users/" + userId + "/profile-photo/file/" + fileName;

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
        String photoUrl = "/ijaa/api/v1/file/users/" + userId + "/cover-photo/file/" + fileName;

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

            String fileUrl = "/ijaa/api/v1/file/events/" + eventId + "/banner/file/" + fileName;

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
            String fileUrl = "/ijaa/api/v1/file/events/" + eventId + "/banner/file/" + banner.getFileName();

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
}
