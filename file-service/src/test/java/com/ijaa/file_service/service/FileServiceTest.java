package com.ijaa.file_service.service;

import com.ijaa.file_service.config.FileStorageConfig;
import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
import com.ijaa.file_service.domain.entity.User;
import com.ijaa.file_service.exceptions.FileStorageException;
import com.ijaa.file_service.exceptions.InvalidFileTypeException;
import com.ijaa.file_service.exceptions.UserNotFoundException;
import com.ijaa.file_service.repository.UserRepository;
import com.ijaa.file_service.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileStorageConfig fileStorageConfig;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FileServiceImpl fileService;

    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_PROFILE_PATH = "./test-uploads/profile";
    private static final String TEST_COVER_PATH = "./test-uploads/cover";

    @BeforeEach
    void setUp() {
        // Configure mock behavior with lenient stubbing
        lenient().when(fileStorageConfig.getProfilePhotosPath()).thenReturn(TEST_PROFILE_PATH);
        lenient().when(fileStorageConfig.getCoverPhotosPath()).thenReturn(TEST_COVER_PATH);
        lenient().when(fileStorageConfig.getAllowedImageTypes()).thenReturn(Arrays.asList("jpg", "jpeg", "png", "webp"));
        lenient().when(fileStorageConfig.getMaxFileSizeMb()).thenReturn(5);
    }

    @Test
    void uploadProfilePhoto_Success() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", "test image content".getBytes()
        );

        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setUsername(TEST_USER_ID);
        user.setActive(true);

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        FileUploadResponse response = fileService.uploadProfilePhoto(TEST_USER_ID, file);

        // Assert
        assertNotNull(response);
        assertEquals("Profile photo uploaded successfully", response.getMessage());
        assertNotNull(response.getFileUrl());
        assertTrue(response.getFileUrl().contains("/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/"));
        assertTrue(response.getFileName().endsWith(".jpg"));
        assertEquals(file.getSize(), response.getFileSize());

        verify(userRepository).findByUserId(TEST_USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void uploadCoverPhoto_Success() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "cover.jpg", "image/jpeg", "test cover image content".getBytes()
        );

        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setUsername(TEST_USER_ID);
        user.setActive(true);

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        FileUploadResponse response = fileService.uploadCoverPhoto(TEST_USER_ID, file);

        // Assert
        assertNotNull(response);
        assertEquals("Cover photo uploaded successfully", response.getMessage());
        assertNotNull(response.getFileUrl());
        assertTrue(response.getFileUrl().contains("/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/"));
        assertTrue(response.getFileName().endsWith(".jpg"));
        assertEquals(file.getSize(), response.getFileSize());

        verify(userRepository).findByUserId(TEST_USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void uploadProfilePhoto_InvalidFileType_ThrowsException() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test content".getBytes()
        );

        // Act & Assert
        assertThrows(InvalidFileTypeException.class, () -> {
            fileService.uploadProfilePhoto(TEST_USER_ID, file);
        });
    }

    @Test
    void uploadProfilePhoto_EmptyFile_ThrowsException() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", new byte[0]
        );

        // Act & Assert
        assertThrows(InvalidFileTypeException.class, () -> {
            fileService.uploadProfilePhoto(TEST_USER_ID, file);
        });
    }

    @Test
    void uploadProfilePhoto_FileTooLarge_ThrowsException() {
        // Arrange
        byte[] largeContent = new byte[6 * 1024 * 1024]; // 6MB
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", largeContent
        );

        // Act & Assert
        assertThrows(InvalidFileTypeException.class, () -> {
            fileService.uploadProfilePhoto(TEST_USER_ID, file);
        });
    }

    @Test
    void getProfilePhotoUrl_UserExistsWithPhoto_ReturnsUrl() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setProfilePhotoPath(TEST_PROFILE_PATH + "/test-photo.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        PhotoUrlResponse response = fileService.getProfilePhotoUrl(TEST_USER_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isExists());
        assertEquals("Profile photo found", response.getMessage());
        assertTrue(response.getPhotoUrl().contains("/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-photo.jpg"));

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getProfilePhotoUrl_UserExistsWithoutPhoto_ReturnsNoPhoto() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setProfilePhotoPath(null);

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        PhotoUrlResponse response = fileService.getProfilePhotoUrl(TEST_USER_ID);

        // Assert
        assertNotNull(response);
        assertFalse(response.isExists());
        assertEquals("No profile photo found", response.getMessage());
        assertNull(response.getPhotoUrl());

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getProfilePhotoUrl_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            fileService.getProfilePhotoUrl(TEST_USER_ID);
        });

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getCoverPhotoUrl_UserExistsWithPhoto_ReturnsUrl() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setCoverPhotoPath(TEST_COVER_PATH + "/test-cover.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        PhotoUrlResponse response = fileService.getCoverPhotoUrl(TEST_USER_ID);

        // Assert
        assertNotNull(response);
        assertTrue(response.isExists());
        assertEquals("Cover photo found", response.getMessage());
        assertTrue(response.getPhotoUrl().contains("/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-cover.jpg"));

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getCoverPhotoUrl_UserExistsWithoutPhoto_ReturnsNoPhoto() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setCoverPhotoPath(null);

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        PhotoUrlResponse response = fileService.getCoverPhotoUrl(TEST_USER_ID);

        // Assert
        assertNotNull(response);
        assertFalse(response.isExists());
        assertEquals("No cover photo found", response.getMessage());
        assertNull(response.getPhotoUrl());

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void deleteProfilePhoto_UserExistsWithPhoto_DeletesSuccessfully() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setProfilePhotoPath(TEST_PROFILE_PATH + "/test-photo.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        assertDoesNotThrow(() -> fileService.deleteProfilePhoto(TEST_USER_ID));

        // Assert
        verify(userRepository).findByUserId(TEST_USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteCoverPhoto_UserExistsWithPhoto_DeletesSuccessfully() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setCoverPhotoPath(TEST_COVER_PATH + "/test-cover.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        fileService.deleteCoverPhoto(TEST_USER_ID);

        // Assert
        verify(userRepository).findByUserId(TEST_USER_ID);
        verify(userRepository).save(user);
        assertNull(user.getCoverPhotoPath());
    }

    @Test
    void uploadProfilePhoto_ReplaceExistingPhoto_DeletesOldFile() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "new-photo.jpg", "image/jpeg", "new image content".getBytes()
        );

        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setProfilePhotoPath(TEST_PROFILE_PATH + "/old-photo.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        FileUploadResponse response = fileService.uploadProfilePhoto(TEST_USER_ID, file);

        // Assert
        assertNotNull(response);
        assertEquals("Profile photo uploaded successfully", response.getMessage());
        assertTrue(response.getFileUrl().contains("/uploads/profile/"));

        verify(userRepository).findByUserId(TEST_USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void uploadCoverPhoto_ReplaceExistingPhoto_DeletesOldFile() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "new-cover.jpg", "image/jpeg", "new cover content".getBytes()
        );

        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setCoverPhotoPath(TEST_COVER_PATH + "/old-cover.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        FileUploadResponse response = fileService.uploadCoverPhoto(TEST_USER_ID, file);

        // Assert
        assertNotNull(response);
        assertEquals("Cover photo uploaded successfully", response.getMessage());
        assertTrue(response.getFileUrl().contains("/uploads/cover/"));

        verify(userRepository).findByUserId(TEST_USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getProfilePhotoFile_Success() throws Exception {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setProfilePhotoPath(TEST_PROFILE_PATH + "/test-photo.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        Resource resource = fileService.getProfilePhotoFile(TEST_USER_ID, "test-photo.jpg");

        // Assert
        assertNotNull(resource);
        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getProfilePhotoFile_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            fileService.getProfilePhotoFile(TEST_USER_ID, "test-photo.jpg");
        });

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getProfilePhotoFile_NoPhoto_ThrowsException() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setProfilePhotoPath(null);

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(FileStorageException.class, () -> {
            fileService.getProfilePhotoFile(TEST_USER_ID, "test-photo.jpg");
        });

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getCoverPhotoFile_Success() throws Exception {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setCoverPhotoPath(TEST_COVER_PATH + "/test-cover.jpg");

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act
        Resource resource = fileService.getCoverPhotoFile(TEST_USER_ID, "test-cover.jpg");

        // Assert
        assertNotNull(resource);
        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getCoverPhotoFile_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> {
            fileService.getCoverPhotoFile(TEST_USER_ID, "test-cover.jpg");
        });

        verify(userRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getCoverPhotoFile_NoPhoto_ThrowsException() {
        // Arrange
        User user = new User();
        user.setUserId(TEST_USER_ID);
        user.setCoverPhotoPath(null);

        when(userRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(FileStorageException.class, () -> {
            fileService.getCoverPhotoFile(TEST_USER_ID, "test-cover.jpg");
        });

        verify(userRepository).findByUserId(TEST_USER_ID);
    }
}
