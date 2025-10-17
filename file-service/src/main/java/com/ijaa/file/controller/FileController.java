package com.ijaa.file.controller;

import com.ijaa.file.common.annotation.RequiresFeature;
import com.ijaa.file.common.utils.AppUtils;
import com.ijaa.file.config.FeatureFlagUtils;
import com.ijaa.file.domain.common.ApiResponse;
import com.ijaa.file.domain.dto.FileUploadResponse;
import com.ijaa.file.domain.dto.PhotoUrlResponse;
import com.ijaa.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Management")
@SecurityRequirement(name = "bearerAuth")
public class FileController {

    private final FileService fileService;
    private final FeatureFlagUtils featureFlagUtils;

    @PostMapping(value = "/{userId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequiresFeature("file-upload.profile-photo")
    @Operation(
            summary = "Upload Profile Photo",
            description = "Upload or update a user's profile photo. Supports JPG, JPEG, PNG, WEBP formats up to 5MB."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile photo uploaded successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Profile photo uploaded successfully",
                                "data": {
                                    "message": "Profile photo uploaded successfully",
                                    "fileUrl": "/ijaa/api/v1/files/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg",
                                    "fileName": "abc123.jpg",
                                    "fileSize": 12345
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid file type or size",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "File storage error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadProfilePhoto(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3")
            @PathVariable String userId,
            @Parameter(
                    description = "Profile photo file (JPG, JPEG, PNG, WEBP, max 5MB)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestPart("file") MultipartFile file) {

        log.info("Received profile photo upload request for user: {}", userId);

        // Validate file is not null or empty
        if (file == null || file.isEmpty()) {
            log.warn("Profile photo upload attempted with null or empty file for user: {}", userId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is required. Please provide a valid image file."));
        }

        try {
            FileUploadResponse response = fileService.uploadProfilePhoto(userId, file);
            featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_UPLOAD_PROFILE_PHOTO, userId);
            return ResponseEntity.ok(ApiResponse.success("Profile photo uploaded successfully", response));
        } catch (Exception e) {
            log.error("Error uploading profile photo for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload profile photo: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/{userId}/cover-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequiresFeature("file-upload.cover-photo")
    @Operation(
            summary = "Upload Cover Photo",
            description = "Upload or update a user's cover photo. Supports JPG, JPEG, PNG, WEBP formats up to 5MB."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Cover photo uploaded successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Cover photo uploaded successfully",
                                "data": {
                                    "message": "Cover photo uploaded successfully",
                                    "fileUrl": "/ijaa/api/v1/files/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/abc123.jpg",
                                    "fileName": "abc123.jpg",
                                    "fileSize": 12345
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    )
                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid file type or size",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "File storage error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadCoverPhoto(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3")
            @PathVariable String userId,
            @Parameter(
                    description = "Cover photo file (JPG, JPEG, PNG, WEBP, max 5MB)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestPart("file") MultipartFile file) {

        log.info("Received cover photo upload request for user: {}, file: {}, size: {}",
                userId, file != null ? file.getOriginalFilename() : "null",
                file != null ? file.getSize() : 0);

        // Enhanced validation
        if (file == null) {
            log.warn("Cover photo upload attempted with null file for user: {}", userId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File parameter is missing. Please provide a file."));
        }

        if (file.isEmpty()) {
            log.warn("Cover photo upload attempted with empty file for user: {}", userId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is empty. Please select a valid image file."));
        }

        // Additional validation for file name
        if (file.getOriginalFilename() == null || file.getOriginalFilename().trim().isEmpty()) {
            log.warn("Cover photo upload attempted with invalid filename for user: {}", userId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid file name. Please select a valid image file."));
        }

        try {
            FileUploadResponse response = fileService.uploadCoverPhoto(userId, file);
            featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_UPLOAD_COVER_PHOTO, userId);
            return ResponseEntity.ok(ApiResponse.success("Cover photo uploaded successfully", response));
        } catch (Exception e) {
            log.error("Error uploading cover photo for user: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload cover photo: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}/profile-photo")
    @RequiresFeature("file-download")
    @Operation(
            summary = "Get Profile Photo URL",
            description = "Retrieve the URL of a user's profile photo. Returns null if no photo exists."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile photo URL retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Photo Exists",
                                            value = """
                            {
                                "success": true,
                                "message": "Profile photo URL retrieved successfully",
                                "data": {
                                    "photoUrl": "/ijaa/api/v1/files/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/profile-photo/file/abc123.jpg",
                                    "message": "Profile photo found",
                                    "exists": true
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "No Photo",
                                            value = """
                            {
                                "success": true,
                                "message": "Profile photo URL retrieved successfully",
                                "data": {
                                    "photoUrl": null,
                                    "message": "No profile photo found",
                                    "exists": false
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<ApiResponse<PhotoUrlResponse>> getProfilePhotoUrl(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId) {

        log.info("Received profile photo URL request for user: {}", userId);

        PhotoUrlResponse response = fileService.getProfilePhotoUrl(userId);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_DOWNLOAD, userId);

        return ResponseEntity.ok(ApiResponse.success("Profile photo URL retrieved successfully", response));
    }

    @GetMapping("/{userId}/cover-photo")
    @RequiresFeature("file-download")
    @Operation(
            summary = "Get Cover Photo URL",
            description = "Retrieve the URL of a user's cover photo. Returns null if no photo exists."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Cover photo URL retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Photo Exists",
                                            value = """
                            {
                                "success": true,
                                "message": "Cover photo URL retrieved successfully",
                                "data": {
                                    "photoUrl": "/ijaa/api/v1/users/e76dbb07-7790-4862-95b0-e0aa96f7b2a3/cover-photo/file/abc123.jpg",
                                    "message": "Cover photo found",
                                    "exists": true
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "No Photo",
                                            value = """
                            {
                                "success": true,
                                "message": "Cover photo URL retrieved successfully",
                                "data": {
                                    "photoUrl": null,
                                    "message": "No cover photo found",
                                    "exists": false
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<ApiResponse<PhotoUrlResponse>> getCoverPhotoUrl(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId) {

        log.info("Received cover photo URL request for user: {}", userId);

        PhotoUrlResponse response = fileService.getCoverPhotoUrl(userId);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_DOWNLOAD, userId);

        return ResponseEntity.ok(ApiResponse.success("Cover photo URL retrieved successfully", response));
    }

    @DeleteMapping("/{userId}/profile-photo")
    @RequiresFeature("file-delete")
    @Operation(
            summary = "Delete Profile Photo",
            description = "Delete a user's profile photo. Removes the file from storage and clears the database reference."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile photo deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Profile photo deleted successfully",
                                "data": null,
                                "timestamp": 1640995200000
                            }
                            """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteProfilePhoto(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId) {

        log.info("Received profile photo deletion request for user: {}", userId);

        fileService.deleteProfilePhoto(userId);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_DELETE, userId);

        return ResponseEntity.ok(ApiResponse.success("Profile photo deleted successfully"));
    }

    @DeleteMapping("/{userId}/cover-photo")
    @RequiresFeature("file-delete")
    @Operation(
            summary = "Delete Cover Photo",
            description = "Delete a user's cover photo. Removes the file from storage and clears the database reference."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Cover photo deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Cover photo deleted successfully",
                                "data": null,
                                "timestamp": 1640995200000
                            }
                            """
                                    )
                            }
                    )
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteCoverPhoto(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId) {

        log.info("Received cover photo deletion request for user: {}", userId);

        fileService.deleteCoverPhoto(userId);
        featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_DELETE, userId);

        return ResponseEntity.ok(ApiResponse.success("Cover photo deleted successfully", null));
    }

    @GetMapping("/{userId}/profile-photo/file/{fileName}")
    @RequiresFeature("file-download")
    @Operation(
            summary = "Get Profile Photo File",
            description = "Serve the actual profile photo file. This endpoint returns the image file directly. **Public endpoint - no authentication required.**"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile photo file served successfully",
                    content = @Content(mediaType = "image/*")
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Profile photo file not found"
            )
    })
    public ResponseEntity<Resource> getProfilePhotoFile(
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId,
            @Parameter(description = "File name", example = "abc123.jpg") @PathVariable String fileName) {

        log.info("Received profile photo file request for user: {}, file: {}", userId, fileName);

        try {
            Resource resource = fileService.getProfilePhotoFile(userId, fileName);
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Adjust based on file type
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error serving profile photo file for user: {}, file: {}", userId, fileName, e);
            return ResponseEntity.notFound().build();
        }
    }

        @GetMapping("/{userId}/cover-photo/file/{fileName}")
    @RequiresFeature("file-download")
    @Operation(
        summary = "Get Cover Photo File",
        description = "Serve the actual cover photo file. This endpoint returns the image file directly. **Public endpoint - no authentication required.**"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Cover photo file served successfully",
            content = @Content(mediaType = "image/*")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Cover photo file not found"
        )
    })
    public ResponseEntity<Resource> getCoverPhotoFile(
        @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId,
        @Parameter(description = "File name", example = "abc123.jpg") @PathVariable String fileName) {

        log.info("Received cover photo file request for user: {}, file: {}", userId, fileName);

        try {
            Resource resource = fileService.getCoverPhotoFile(userId, fileName);
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Adjust based on file type
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error serving cover photo file for user: {}, file: {}", userId, fileName, e);
            return ResponseEntity.notFound().build();
        }
    }
}
