package com.ijaa.file_service.controller;

import com.ijaa.file_service.config.FeatureFlagUtils;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Event Banner Management", description = "APIs for event banner image management")
public class EventBannerController {

    private final FileService fileService;
    private final FeatureFlagUtils featureFlagUtils;

    @PostMapping(value = "/{eventId}/banner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload Event Banner",
            description = "Upload or update an event banner image. Supports JPG, JPEG, PNG, WEBP formats up to 5MB. Old banner will be automatically replaced.\n\n" +
                    "**Important**: In Swagger UI, make sure to:\n" +
                    "1. Click 'Choose File' and select your image\n" +
                    "2. The file parameter name should be 'file'\n" +
                    "3. File size should be under 5MB"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Event banner uploaded successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Event banner uploaded successfully",
                                "data": {
                                    "message": "Event banner uploaded successfully",
                                    "fileUrl": "/ijaa/api/v1/events/1/banner/file/abc123.jpg",
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
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadEventBanner(
            @Parameter(description = "Event ID", example = "1")
            @PathVariable String eventId,
            @Parameter(
                    description = "Event banner file (JPG, JPEG, PNG, WEBP, max 5MB)",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
                    schema = @Schema(type = "string", format = "binary")
            )
            @RequestPart("file") MultipartFile file) {

        log.info("Received event banner upload request for event: {}", eventId);

        // Validate file is not null or empty
        if (file == null || file.isEmpty()) {
            log.warn("Event banner upload attempted with null or empty file for event: {}", eventId);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("File is required. Please provide a valid image file."));
        }

        try {
            log.debug("Starting event banner upload for event: {}", eventId);
            FileUploadResponse response = fileService.uploadEventBanner(eventId, file);
            log.debug("Event banner upload completed successfully for event: {}", eventId);
            featureFlagUtils.logFeatureUsage(FeatureFlagUtils.FILE_UPLOAD_EVENT_BANNER, eventId);
            return ResponseEntity.ok(ApiResponse.success("Event banner uploaded successfully", response));
        } catch (Exception e) {
            log.error("Error uploading event banner for event: {} - Error: {} - Stack trace: {}", 
                     eventId, e.getMessage(), e.getStackTrace(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload event banner: " + e.getMessage()));
        }
    }

    @GetMapping("/{eventId}/banner")
    @Operation(
            summary = "Get Event Banner URL",
            description = "Retrieve the URL of an event banner. Returns null if no banner exists."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Event banner URL retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Success Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Event banner URL retrieved successfully",
                                "data": {
                                    "photoUrl": "/ijaa/api/v1/events/1/banner/file/abc123.jpg",
                                    "message": "Event banner found",
                                    "exists": true
                                },
                                "timestamp": 1640995200000
                            }
                            """
                                    ),
                                    @ExampleObject(
                                            name = "No Banner Response",
                                            value = """
                            {
                                "success": true,
                                "message": "Event banner URL retrieved successfully",
                                "data": {
                                    "photoUrl": null,
                                    "message": "No event banner found",
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
    public ResponseEntity<ApiResponse<PhotoUrlResponse>> getEventBannerUrl(
            @Parameter(description = "Event ID", example = "1")
            @PathVariable String eventId) {

        log.info("Received event banner URL request for event: {}", eventId);

        try {
            PhotoUrlResponse response = fileService.getEventBannerUrl(eventId);
            return ResponseEntity.ok(ApiResponse.success("Event banner URL retrieved successfully", response));
        } catch (Exception e) {
            log.error("Error getting event banner URL for event: {}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get event banner URL: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{eventId}/banner")
    @Operation(
            summary = "Delete Event Banner",
            description = "Delete an event banner image."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Event banner deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Event banner not found"
            )
    })
    public ResponseEntity<ApiResponse<Void>> deleteEventBanner(
            @Parameter(description = "Event ID", example = "1")
            @PathVariable String eventId) {

        log.info("Received event banner deletion request for event: {}", eventId);

        try {
            fileService.deleteEventBanner(eventId);
            return ResponseEntity.ok(ApiResponse.success("Event banner deleted successfully", null));
        } catch (Exception e) {
            log.error("Error deleting event banner for event: {}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete event banner: " + e.getMessage()));
        }
    }

    @GetMapping("/{eventId}/banner/file/{fileName}")
    @Operation(
            summary = "Get Event Banner File",
            description = "Serve the actual event banner file. This endpoint returns the image file directly. **Public endpoint - no authentication required.**"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Event banner file served successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Event banner file not found"
            )
    })
    public ResponseEntity<Resource> getEventBannerFile(
            @Parameter(description = "Event ID", example = "1")
            @PathVariable String eventId,
            @Parameter(description = "File name", example = "abc123.jpg")
            @PathVariable String fileName) {

        log.info("Received event banner file request for event: {}, file: {}", eventId, fileName);

        try {
            Resource resource = fileService.getEventBannerFile(eventId, fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Adjust based on file type
                    .body(resource);
        } catch (Exception e) {
            log.error("Error serving event banner file for event: {}, file: {}", eventId, fileName, e);
            return ResponseEntity.notFound().build();
        }
    }
}




