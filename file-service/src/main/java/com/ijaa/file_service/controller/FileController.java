package com.ijaa.file_service.controller;

import com.ijaa.file_service.domain.common.ApiResponse;
import com.ijaa.file_service.domain.dto.FileUploadResponse;
import com.ijaa.file_service.domain.dto.PhotoUrlResponse;
import com.ijaa.file_service.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "File Management", description = "APIs for user profile and cover photo management")
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/{userId}/profile-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload Profile Photo",
        description = "Upload or update a user's profile photo. Supports JPG, JPEG, PNG, WEBP formats up to 5MB. Old photo will be automatically replaced.\n\n" +
                     "**Important**: In Swagger UI, make sure to:\n" +
                     "1. Click 'Choose File' and select your image\n" +
                     "2. The file parameter name should be 'file'\n" +
                     "3. File size should be under 5MB",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Profile photo file (multipart/form-data)",
            required = true,
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(type = "string", format = "binary"),
                encoding = {
                    @io.swagger.v3.oas.annotations.media.Encoding(
                        name = "file",
                        contentType = "image/jpeg, image/png, image/webp"
                    )
                }
            )
        )
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
                                    "filePath": "/uploads/profile/abc123.jpg",
                                    "fileUrl": "/uploads/profile/abc123.jpg",
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
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId,
            @Parameter(description = "Profile photo file (JPG, JPEG, PNG, WEBP, max 5MB)") @RequestParam("file") MultipartFile file) {

        log.info("Received profile photo upload request for user: {}", userId);

        // Validate file is not null or empty
        if (file == null || file.isEmpty()) {
            log.warn("Profile photo upload attempted with null or empty file for user: {}", userId);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("File is required. Please provide a valid image file."));
        }

        FileUploadResponse response = fileService.uploadProfilePhoto(userId, file);

        return ResponseEntity.ok(ApiResponse.success("Profile photo uploaded successfully", response));
    }

    @PostMapping(value = "/{userId}/cover-photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Upload Cover Photo",
        description = "Upload or update a user's cover photo. Supports JPG, JPEG, PNG, WEBP formats up to 5MB. Old photo will be automatically replaced.\n\n" +
                     "**Important**: In Swagger UI, make sure to:\n" +
                     "1. Click 'Choose File' and select your image\n" +
                     "2. The file parameter name should be 'file'\n" +
                     "3. File size should be under 5MB",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cover photo file (multipart/form-data)",
            required = true,
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(type = "string", format = "binary"),
                encoding = {
                    @io.swagger.v3.oas.annotations.media.Encoding(
                        name = "file",
                        contentType = "image/jpeg, image/png, image/webp"
                    )
                }
            )
        )
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
                                    "filePath": "/uploads/cover/abc123.jpg",
                                    "fileUrl": "/uploads/cover/abc123.jpg",
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
            @Parameter(description = "User ID", example = "e76dbb07-7790-4862-95b0-e0aa96f7b2a3") @PathVariable String userId,
            @Parameter(description = "Cover photo file (JPG, JPEG, PNG, WEBP, max 5MB)") @RequestParam("file") MultipartFile file) {

        log.info("Received cover photo upload request for user: {}", userId);

        // Validate file is not null or empty
        if (file == null || file.isEmpty()) {
            log.warn("Cover photo upload attempted with null or empty file for user: {}", userId);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("File is required. Please provide a valid image file."));
        }

        FileUploadResponse response = fileService.uploadCoverPhoto(userId, file);

        return ResponseEntity.ok(ApiResponse.success("Cover photo uploaded successfully", response));
    }

    @GetMapping("/{userId}/profile-photo")
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
                                    "photoUrl": "/uploads/profile/abc123.jpg",
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
        
        return ResponseEntity.ok(ApiResponse.success("Profile photo URL retrieved successfully", response));
    }

    @GetMapping("/{userId}/cover-photo")
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
                                    "photoUrl": "/uploads/cover/abc123.jpg",
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
        
        return ResponseEntity.ok(ApiResponse.success("Cover photo URL retrieved successfully", response));
    }

    @DeleteMapping("/{userId}/profile-photo")
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
        
        return ResponseEntity.ok(ApiResponse.success("Profile photo deleted successfully"));
    }

    @DeleteMapping("/{userId}/cover-photo")
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
        
        return ResponseEntity.ok(ApiResponse.success("Cover photo deleted successfully"));
    }
}
