package com.ijaa.file_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
import com.ijaa.file_service.exceptions.InvalidFileTypeException;
import com.ijaa.file_service.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.core.io.Resource;
import com.ijaa.file_service.exceptions.FileStorageException;
import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class FileControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private FileService fileService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private static final String TEST_USER_ID = "test-user-123";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void uploadProfilePhoto_Success() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", "test image content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Profile photo uploaded successfully",
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-file.jpg",
            "test-file.jpg",
            1234L
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any())).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo uploaded successfully"))
                .andExpect(jsonPath("$.data.message").value("Profile photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileUrl").value("/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-file.jpg"));
    }

    @Test
    void uploadCoverPhoto_Success() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
            "file", "cover.jpg", "image/jpeg", "test cover content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Cover photo uploaded successfully",
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-cover.jpg",
            "test-cover.jpg",
            5678L
        );

        when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any())).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo uploaded successfully"))
                .andExpect(jsonPath("$.data.message").value("Cover photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileUrl").value("/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-cover.jpg"));
    }

    @Test
    void getProfilePhotoUrl_Success() throws Exception {
        // Arrange
        PhotoUrlResponse expectedResponse = new PhotoUrlResponse(
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-photo.jpg",
            "Profile photo found",
            true
        );

        when(fileService.getProfilePhotoUrl(TEST_USER_ID)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/profile-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo URL retrieved successfully"))
                .andExpect(jsonPath("$.data.photoUrl").value("/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-photo.jpg"))
                .andExpect(jsonPath("$.data.exists").value(true));
    }

    @Test
    void getCoverPhotoUrl_Success() throws Exception {
        // Arrange
        PhotoUrlResponse expectedResponse = new PhotoUrlResponse(
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-cover.jpg",
            "Cover photo found",
            true
        );

        when(fileService.getCoverPhotoUrl(TEST_USER_ID)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/cover-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo URL retrieved successfully"))
                .andExpect(jsonPath("$.data.photoUrl").value("/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-cover.jpg"))
                .andExpect(jsonPath("$.data.exists").value(true));
    }

    @Test
    void getProfilePhotoUrl_NoPhotoFound() throws Exception {
        // Arrange
        PhotoUrlResponse expectedResponse = new PhotoUrlResponse(
            null,
            "No profile photo found",
            false
        );

        when(fileService.getProfilePhotoUrl(TEST_USER_ID)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/profile-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.exists").value(false))
                .andExpect(jsonPath("$.data.photoUrl").isEmpty());
    }

    @Test
    void getCoverPhotoUrl_NoPhotoFound() throws Exception {
        // Arrange
        PhotoUrlResponse expectedResponse = new PhotoUrlResponse(
            null,
            "No cover photo found",
            false
        );

        when(fileService.getCoverPhotoUrl(TEST_USER_ID)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/cover-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.exists").value(false))
                .andExpect(jsonPath("$.data.photoUrl").isEmpty());
    }

    @Test
    void deleteProfilePhoto_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{userId}/profile-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo deleted successfully"));
    }

    @Test
    void deleteCoverPhoto_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/users/{userId}/cover-photo", TEST_USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo deleted successfully"));
    }

    @Test
    void uploadProfilePhoto_InvalidFileType_ReturnsBadRequest() throws Exception {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test content".getBytes()
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any()))
            .thenThrow(new InvalidFileTypeException("Invalid file type. Only JPG, JPEG, PNG, WEBP are allowed"));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(invalidFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadProfilePhoto_NoFile_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID))
                .andExpect(status().isBadRequest()) // Now handled by controller validation
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a file to upload."));
    }

    @Test
    void uploadCoverPhoto_NoFile_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID))
                .andExpect(status().isBadRequest()) // Now handled by controller validation
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a file to upload."));
    }

    @Test
    void uploadCoverPhoto_EmptyFile_ReturnsBadRequest() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", new byte[0]
        );

        when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any()))
            .thenThrow(new InvalidFileTypeException("File cannot be empty"));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(emptyFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadCoverPhoto_InvalidFileType_ReturnsBadRequest() throws Exception {
        // Arrange
        MockMultipartFile invalidFile = new MockMultipartFile(
            "file", "test.txt", "text/plain", "test content".getBytes()
        );

        when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any()))
            .thenThrow(new InvalidFileTypeException("Invalid file type. Only JPG, JPEG, PNG, WEBP are allowed"));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(invalidFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadProfilePhoto_ValidFile_ReturnsSuccess() throws Exception {
        // Arrange
        MockMultipartFile validFile = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", "test image content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Profile photo uploaded successfully",
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-uuid.jpg",
            "test-uuid.jpg",
            1024L
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any()))
            .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(validFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileName").value("test-uuid.jpg"));
    }

    @Test
    void uploadCoverPhoto_ValidFile_ReturnsSuccess() throws Exception {
        // Arrange
        MockMultipartFile validFile = new MockMultipartFile(
            "file", "test.png", "image/png", "test image content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Cover photo uploaded successfully",
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-uuid.png",
            "test-uuid.png",
            2048L
        );

        when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any()))
            .thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(validFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileName").value("test-uuid.png"));
    }

    @Test
    void uploadProfilePhoto_SwaggerUI_Scenario_ValidFile() throws Exception {
        // Test scenario that mimics Swagger UI file upload
        MockMultipartFile swaggerFile = new MockMultipartFile(
            "file", "swagger-test.jpg", "image/jpeg", "swagger test content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Profile photo uploaded successfully",
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/swagger-uuid.jpg",
            "swagger-uuid.jpg",
            1536L
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any()))
            .thenReturn(expectedResponse);

        // Act & Assert - This should work exactly like Swagger UI
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(swaggerFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileName").value("swagger-uuid.jpg"));
    }

    @Test
    void uploadCoverPhoto_SwaggerUI_Scenario_ValidFile() throws Exception {
        // Test scenario that mimics Swagger UI file upload
        MockMultipartFile swaggerFile = new MockMultipartFile(
            "file", "swagger-cover.png", "image/png", "swagger cover test content".getBytes()
        );

        FileUploadResponse expectedResponse = new FileUploadResponse(
            "Cover photo uploaded successfully",
            "/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/swagger-cover-uuid.png",
            "swagger-cover-uuid.png",
            2048L
        );

        when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any()))
            .thenReturn(expectedResponse);

        // Act & Assert - This should work exactly like Swagger UI
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(swaggerFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileName").value("swagger-cover-uuid.png"));
    }

    @Test
    void uploadProfilePhoto_SwaggerUI_Scenario_NoFile_ReturnsBadRequest() throws Exception {
        // Test scenario that mimics Swagger UI when no file is selected
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a file to upload."));
    }

    @Test
    void uploadCoverPhoto_SwaggerUI_Scenario_NoFile_ReturnsBadRequest() throws Exception {
        // Test scenario that mimics Swagger UI when no file is selected
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a file to upload."));
    }

    @Test
    void uploadProfilePhoto_DifferentFileTypes_AllSupported() throws Exception {
        // Test all supported file types
        String[] supportedTypes = {"jpg", "jpeg", "png", "webp"};
        String[] mimeTypes = {"image/jpeg", "image/jpeg", "image/png", "image/webp"};

        for (int i = 0; i < supportedTypes.length; i++) {
            MockMultipartFile file = new MockMultipartFile(
                "file", "test." + supportedTypes[i], mimeTypes[i], "test content".getBytes()
            );

            FileUploadResponse expectedResponse = new FileUploadResponse(
                "Profile photo uploaded successfully",
                "/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/test-uuid." + supportedTypes[i],
                "test-uuid." + supportedTypes[i],
                1024L
            );

            when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any()))
                .thenReturn(expectedResponse);

            mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                    .file(file))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.fileName").value("test-uuid." + supportedTypes[i]));
        }
    }

    @Test
    void uploadCoverPhoto_DifferentFileTypes_AllSupported() throws Exception {
        // Test all supported file types
        String[] supportedTypes = {"jpg", "jpeg", "png", "webp"};
        String[] mimeTypes = {"image/jpeg", "image/jpeg", "image/png", "image/webp"};

        for (int i = 0; i < supportedTypes.length; i++) {
            MockMultipartFile file = new MockMultipartFile(
                "file", "test." + supportedTypes[i], mimeTypes[i], "test content".getBytes()
            );

            FileUploadResponse expectedResponse = new FileUploadResponse(
                "Cover photo uploaded successfully",
                "/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/test-uuid." + supportedTypes[i],
                "test-uuid." + supportedTypes[i],
                1024L
            );

            when(fileService.uploadCoverPhoto(eq(TEST_USER_ID), any()))
                .thenReturn(expectedResponse);

            mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                    .file(file))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.fileName").value("test-uuid." + supportedTypes[i]));
        }
    }

    @Test
    void uploadProfilePhoto_EmptyFile_ReturnsBadRequest() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", "test.jpg", "image/jpeg", new byte[0]
        );

        when(fileService.uploadProfilePhoto(eq(TEST_USER_ID), any()))
            .thenThrow(new InvalidFileTypeException("File cannot be empty"));

        // Act & Assert
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File parameter is missing. Please provide a file."));
    }

    @Test
    void getProfilePhotoFile_Success() throws Exception {
        // Arrange
        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.isReadable()).thenReturn(true);
        when(fileService.getProfilePhotoFile(TEST_USER_ID, "test-photo.jpg")).thenReturn(mockResource);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/profile-photo/file/{fileName}", TEST_USER_ID, "test-photo.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/jpeg"));
    }

    @Test
    void getProfilePhotoFile_NotFound() throws Exception {
        // Arrange
        when(fileService.getProfilePhotoFile(TEST_USER_ID, "nonexistent.jpg"))
            .thenThrow(new FileStorageException("Profile photo file not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/profile-photo/file/{fileName}", TEST_USER_ID, "nonexistent.jpg"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCoverPhotoFile_Success() throws Exception {
        // Arrange
        Resource mockResource = mock(Resource.class);
        when(mockResource.exists()).thenReturn(true);
        when(mockResource.isReadable()).thenReturn(true);
        when(fileService.getCoverPhotoFile(TEST_USER_ID, "test-cover.jpg")).thenReturn(mockResource);

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/cover-photo/file/{fileName}", TEST_USER_ID, "test-cover.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/jpeg"));
    }

    @Test
    void getCoverPhotoFile_NotFound() throws Exception {
        // Arrange
        when(fileService.getCoverPhotoFile(TEST_USER_ID, "nonexistent.jpg"))
            .thenThrow(new FileStorageException("Cover photo file not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/users/{userId}/cover-photo/file/{fileName}", TEST_USER_ID, "nonexistent.jpg"))
                .andExpect(status().isNotFound());
    }
}
