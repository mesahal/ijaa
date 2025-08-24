package com.ijaa.file_service.service.impl;

import com.ijaa.file_service.config.FileStorageConfig;
import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
import com.ijaa.file_service.domain.entity.User;
import com.ijaa.file_service.exceptions.FileStorageException;
import com.ijaa.file_service.exceptions.InvalidFileTypeException;
import com.ijaa.file_service.exceptions.UserNotFoundException;
import com.ijaa.file_service.repository.UserRepository;
import com.ijaa.file_service.service.FileService;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileStorageConfig fileStorageConfig;
    private final UserRepository userRepository;

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
                deleteFile(Paths.get(user.getProfilePhotoPath()));
            }
            
            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);
            
            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Update user record
            user.setProfilePhotoPath(filePath.toString());
            userRepository.save(user);
            
            String fileUrl = "/uploads/profile/" + fileName;
            
            log.info("Profile photo uploaded successfully for user: {}, file: {}", userId, fileName);
            
            return new FileUploadResponse(
                "Profile photo uploaded successfully",
                filePath.toString(),
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
                deleteFile(Paths.get(user.getCoverPhotoPath()));
            }
            
            // Generate unique filename
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            Path filePath = uploadDir.resolve(fileName);
            
            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Update user record
            user.setCoverPhotoPath(filePath.toString());
            userRepository.save(user);
            
            String fileUrl = "/uploads/cover/" + fileName;
            
            log.info("Cover photo uploaded successfully for user: {}, file: {}", userId, fileName);
            
            return new FileUploadResponse(
                "Cover photo uploaded successfully",
                filePath.toString(),
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
        
        String fileName = Paths.get(user.getProfilePhotoPath()).getFileName().toString();
        String photoUrl = "/uploads/profile/" + fileName;
        
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
        
        String fileName = Paths.get(user.getCoverPhotoPath()).getFileName().toString();
        String photoUrl = "/uploads/cover/" + fileName;
        
        return new PhotoUrlResponse(photoUrl, "Cover photo found", true);
    }

    @Override
    public void deleteProfilePhoto(String userId) {
        log.info("Deleting profile photo for user: {}", userId);
        
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with userId: " + userId));
        
        if (user.getProfilePhotoPath() != null) {
            try {
                deleteFile(Paths.get(user.getProfilePhotoPath()));
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
                deleteFile(Paths.get(user.getCoverPhotoPath()));
                user.setCoverPhotoPath(null);
                userRepository.save(user);
                log.info("Cover photo deleted successfully for user: {}", userId);
            } catch (Exception e) {
                log.error("Error deleting cover photo for user: {}", userId, e);
                throw new FileStorageException("Failed to delete cover photo", e);
            }
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
}
