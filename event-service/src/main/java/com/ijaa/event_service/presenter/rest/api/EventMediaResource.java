package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.EventMediaRequest;
import com.ijaa.event_service.domain.response.EventMediaResponse;
import com.ijaa.event_service.service.EventMediaService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/events/media")
@RequiredArgsConstructor
@Tag(name = "Event Media", description = "Event media management APIs")
public class EventMediaResource {

    private final EventMediaService eventMediaService;

    @PostMapping
    @Operation(
        summary = "Upload Event Media",
        description = "Upload media files (images, videos, documents) for an event",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Event media upload details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventMediaRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Image Upload",
                        summary = "Upload an image for an event",
                        value = """
                            {
                                "eventId": 1,
                                "mediaType": "IMAGE",
                                "mediaUrl": "https://example.com/images/event-photo.jpg",
                                "title": "Event Photo",
                                "description": "Group photo from the alumni meet"
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Document Upload",
                        summary = "Upload a document for an event",
                        value = """
                            {
                                "eventId": 1,
                                "mediaType": "DOCUMENT",
                                "mediaUrl": "https://example.com/documents/event-agenda.pdf",
                                "title": "Event Agenda",
                                "description": "Detailed agenda for the alumni meet"
                            }
                            """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Media uploaded successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Media uploaded successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "eventId": 1,
                                    "mediaType": "IMAGE",
                                    "mediaUrl": "https://example.com/images/event-photo.jpg",
                                    "title": "Event Photo",
                                    "description": "Group photo from the alumni meet",
                                    "uploadedBy": "john.doe",
                                    "uploadedAt": "2024-12-01T10:00:00",
                                    "fileSize": "2.5MB",
                                    "fileFormat": "JPEG"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid media data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid media data provided",
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
    public ResponseEntity<ApiResponse<EventMediaResponse>> uploadMedia(
            @Valid @RequestBody EventMediaRequest request,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventMediaResponse response = eventMediaService.uploadMedia(request, username);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Media uploaded successfully", "201", response));
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get event media", description = "Get all media for an event")
    public ResponseEntity<ApiResponse<List<EventMediaResponse>>> getEventMedia(@PathVariable Long eventId) {
        
        List<EventMediaResponse> response = eventMediaService.getEventMedia(eventId);
        
        return ResponseEntity.ok(new ApiResponse<>("Event media retrieved successfully", "200", response));
    }

    @GetMapping("/event/{eventId}/type/{mediaType}")
    @Operation(summary = "Get event media by type", description = "Get media for an event by type")
    public ResponseEntity<ApiResponse<List<EventMediaResponse>>> getEventMediaByType(
            @PathVariable Long eventId,
            @PathVariable String mediaType) {
        
        List<EventMediaResponse> response = eventMediaService.getEventMediaByType(eventId, mediaType);
        
        return ResponseEntity.ok(new ApiResponse<>("Event media by type retrieved successfully", "200", response));
    }

    @GetMapping("/{mediaId}")
    @Operation(summary = "Get specific media", description = "Get a specific media item by ID")
    public ResponseEntity<ApiResponse<EventMediaResponse>> getMedia(@PathVariable Long mediaId) {
        
        EventMediaResponse response = eventMediaService.getMedia(mediaId);
        
        return ResponseEntity.ok(new ApiResponse<>("Media retrieved successfully", "200", response));
    }

    @PutMapping("/{mediaId}/caption")
    @Operation(summary = "Update media caption", description = "Update the caption for a media item")
    public ResponseEntity<ApiResponse<EventMediaResponse>> updateMediaCaption(
            @PathVariable Long mediaId,
            @Parameter(description = "New caption") @RequestParam String caption,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventMediaResponse response = eventMediaService.updateMediaCaption(mediaId, caption, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Media caption updated successfully", "200", response));
    }

    @DeleteMapping("/{mediaId}")
    @Operation(summary = "Delete media", description = "Delete a media item")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(
            @PathVariable Long mediaId,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        eventMediaService.deleteMedia(mediaId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Media deleted successfully", "200", null));
    }

    @PostMapping("/{mediaId}/like")
    @Operation(summary = "Like/unlike media", description = "Toggle like status for a media item")
    public ResponseEntity<ApiResponse<EventMediaResponse>> toggleLike(
            @PathVariable Long mediaId,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventMediaResponse response = eventMediaService.toggleLike(mediaId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Like toggled successfully", "200", response));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get user's media", description = "Get paginated media uploaded by a specific user")
    public ResponseEntity<ApiResponse<PagedResponse<EventMediaResponse>>> getUserMedia(
            @PathVariable String username,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventMediaResponse> response = eventMediaService.getUserMedia(username, page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("User media retrieved successfully", "200", response));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent media", description = "Get paginated recent media")
    public ResponseEntity<ApiResponse<PagedResponse<EventMediaResponse>>> getRecentMedia(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventMediaResponse> response = eventMediaService.getRecentMedia(page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("Recent media retrieved successfully", "200", response));
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular media", description = "Get paginated popular media")
    public ResponseEntity<ApiResponse<PagedResponse<EventMediaResponse>>> getPopularMedia(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventMediaResponse> response = eventMediaService.getPopularMedia(page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("Popular media retrieved successfully", "200", response));
    }

    private String extractUsername(String userContext) {
        // This is a simplified implementation
        // In a real app, you'd decode the JWT token or user context
        return userContext; // Assuming userContext contains the username
    }
} 