package com.ijaa.event.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event.common.annotation.RequiresFeature;
import com.ijaa.event.common.annotation.RequiresRole;
import com.ijaa.event.common.utils.AppUtils;
import com.ijaa.event.common.service.BaseService;
import com.ijaa.event.domain.common.ApiResponse;
import com.ijaa.event.domain.dto.FileUploadResponse;
import com.ijaa.event.domain.dto.PhotoUrlResponse;
import com.ijaa.event.service.EventBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(AppUtils.BASE_URL + "/banner")
@Tag(name = "Event Banner")
public class EventBannerResource extends BaseService {

    private final EventBannerService eventBannerService;

    public EventBannerResource(EventBannerService eventBannerService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventBannerService = eventBannerService;
    }

    @PostMapping(value = "/{eventId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequiresRole("USER")
    @RequiresFeature("events.banner")
    @Operation(
        summary = "Upload Event Banner",
        description = "Upload or update banner image for an event (one banner per event) (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Banner uploaded successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid file or file too large",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid File",
                        value = """
                            {
                                "message": "File type not allowed. Allowed types: jpg, jpeg, png, webp",
                                "code": "400",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadBanner(
            @PathVariable String eventId,
            @Parameter(
                description = "Event banner file (JPG, JPEG, PNG, WEBP, max 5MB)",
                content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
                schema = @Schema(type = "string", format = "binary")
            )
            @RequestPart("file") MultipartFile file) {
        
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }

        // Validate file is not null or empty
        if (file == null || file.isEmpty()) {
            log.warn("Event banner upload attempted with null or empty file for event: {}", eventId);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("File is required. Please provide a valid image file.", "400", null));
        }
        
        try {
            FileUploadResponse response = eventBannerService.uploadBanner(eventId, file, username);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("Banner uploaded successfully", "201", response));
        } catch (Exception e) {
            log.error("Error uploading banner for event: {}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to upload banner: " + e.getMessage(), "500", null));
        }
    }

    @GetMapping("/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events.banner")
    @Operation(
        summary = "Get Event Banner URL",
        description = "Get banner URL for a specific event"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Banner URL retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Banner not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "No event banner found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<PhotoUrlResponse>> getBannerUrl(@PathVariable String eventId) {
        
        try {
            PhotoUrlResponse response = eventBannerService.getBannerUrl(eventId);
            return ResponseEntity.ok(new ApiResponse<>("Banner URL retrieved successfully", "200", response));
        } catch (Exception e) {
            log.error("Error getting banner URL for event: {}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to get banner URL: " + e.getMessage(), "500", null));
        }
    }

    @DeleteMapping("/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events.banner")
    @Operation(
        summary = "Delete Event Banner",
        description = "Delete banner image for an event (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Banner deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Event or banner not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Event not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteBanner(@PathVariable String eventId) {
        
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        
        try {
            eventBannerService.deleteBanner(eventId, username);
            return ResponseEntity.ok(new ApiResponse<>("Banner deleted successfully", "200", null));
        } catch (Exception e) {
            log.error("Error deleting banner for event: {}", eventId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Failed to delete banner: " + e.getMessage(), "500", null));
        }
    }
}
