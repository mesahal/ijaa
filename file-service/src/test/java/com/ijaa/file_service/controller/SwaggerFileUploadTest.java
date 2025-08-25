package com.ijaa.file_service.controller;

import com.ijaa.file_service.domain.dto.FileUploadResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
class SwaggerFileUploadTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private FileService fileService;

    private MockMvc mockMvc;

    private static final String TEST_USER_ID = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void swaggerUI_ProfilePhotoUpload_WithValidFile_Success() throws Exception {
        // Simulate Swagger UI file upload scenario
        MockMultipartFile file = new MockMultipartFile(
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

        // This should work exactly like Swagger UI
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Profile photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileUrl").value("/ijaa/api/v1/users/" + TEST_USER_ID + "/profile-photo/file/swagger-uuid.jpg"))
                .andExpect(jsonPath("$.data.fileName").value("swagger-uuid.jpg"));
    }

    @Test
    void swaggerUI_CoverPhotoUpload_WithValidFile_Success() throws Exception {
        // Simulate Swagger UI file upload scenario
        MockMultipartFile file = new MockMultipartFile(
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

        // This should work exactly like Swagger UI
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cover photo uploaded successfully"))
                .andExpect(jsonPath("$.data.fileUrl").value("/ijaa/api/v1/users/" + TEST_USER_ID + "/cover-photo/file/swagger-cover-uuid.png"))
                .andExpect(jsonPath("$.data.fileName").value("swagger-cover-uuid.png"));
    }

    @Test
    void swaggerUI_ProfilePhotoUpload_NoFileSelected_ReturnsBadRequest() throws Exception {
        // Simulate Swagger UI when no file is selected
        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a file to upload."));
    }

    @Test
    void swaggerUI_CoverPhotoUpload_NoFileSelected_ReturnsBadRequest() throws Exception {
        // Simulate Swagger UI when no file is selected
        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a file to upload."));
    }

    @Test
    void swaggerUI_ProfilePhotoUpload_EmptyFile_ReturnsBadRequest() throws Exception {
        // Simulate Swagger UI with empty file
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", "empty.jpg", "image/jpeg", new byte[0]
        );

        mockMvc.perform(multipart("/api/v1/users/{userId}/profile-photo", TEST_USER_ID)
                .file(emptyFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a valid image file."));
    }

    @Test
    void swaggerUI_CoverPhotoUpload_EmptyFile_ReturnsBadRequest() throws Exception {
        // Simulate Swagger UI with empty file
        MockMultipartFile emptyFile = new MockMultipartFile(
            "file", "empty.png", "image/png", new byte[0]
        );

        mockMvc.perform(multipart("/api/v1/users/{userId}/cover-photo", TEST_USER_ID)
                .file(emptyFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("File is required. Please provide a valid image file."));
    }

    @Test
    void swaggerUI_ProfilePhotoUpload_AllSupportedFormats() throws Exception {
        // Test all supported formats in Swagger UI scenario
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
                    .file(file)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.fileName").value("test-uuid." + supportedTypes[i]));
        }
    }

    @Test
    void swaggerUI_CoverPhotoUpload_AllSupportedFormats() throws Exception {
        // Test all supported formats in Swagger UI scenario
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
                    .file(file)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.fileName").value("test-uuid." + supportedTypes[i]));
        }
    }
}
