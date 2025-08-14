package com.ijaa.event_service.presenter.rest.api;

import com.ijaa.event_service.domain.common.ApiResponse;
import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.EventCommentRequest;
import com.ijaa.event_service.domain.response.EventCommentResponse;
import com.ijaa.event_service.service.EventCommentService;
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
@RequestMapping("/api/v1/user/events/comments")
@RequiredArgsConstructor
@Tag(name = "Event Comments", description = "Event comment management APIs")
public class EventCommentResource {

    private final EventCommentService eventCommentService;

    @PostMapping
    @Operation(
        summary = "Create Event Comment", 
        description = "Create a new comment for an event with support for replies and threaded discussions",
        security = @SecurityRequirement(name = "Bearer Authentication"),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Comment creation details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventCommentRequest.class),
                examples = {
                    @ExampleObject(
                        name = "Top-level Comment",
                        summary = "Create a new top-level comment",
                        value = """
                            {
                                "eventId": 1,
                                "content": "This event looks amazing! Looking forward to attending.",
                                "parentCommentId": null
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Reply to Comment",
                        summary = "Create a reply to an existing comment",
                        value = """
                            {
                                "eventId": 1,
                                "content": "I agree! The venue is perfect for this gathering.",
                                "parentCommentId": 5
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "Short Comment",
                        summary = "Create a simple comment",
                        value = """
                            {
                                "eventId": 1,
                                "content": "Count me in!"
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
            description = "Comment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event_service.domain.common.ApiResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Success Response",
                        value = """
                            {
                                "message": "Comment created successfully",
                                "code": "201",
                                "data": {
                                    "id": 1,
                                    "eventId": 1,
                                    "content": "This event looks amazing! Looking forward to attending.",
                                    "authorUsername": "john.doe",
                                    "authorName": "John Doe",
                                    "parentCommentId": null,
                                    "likesCount": 0,
                                    "repliesCount": 0,
                                    "isLikedByUser": false,
                                    "createdAt": "2024-12-01T10:00:00",
                                    "updatedAt": "2024-12-01T10:00:00"
                                }
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid comment data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Invalid Request",
                        value = """
                            {
                                "message": "Invalid comment data provided",
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
    public ResponseEntity<ApiResponse<EventCommentResponse>> createComment(
            @Valid @RequestBody EventCommentRequest request,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventCommentResponse response = eventCommentService.createComment(request, username);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Comment created successfully", "201", response));
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get event comments", description = "Get paginated comments for an event")
    public ResponseEntity<ApiResponse<PagedResponse<EventCommentResponse>>> getEventComments(
            @PathVariable Long eventId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventCommentResponse> response = eventCommentService.getEventComments(eventId, page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("Comments retrieved successfully", "200", response));
    }

    @GetMapping("/event/{eventId}/all")
    @Operation(summary = "Get all event comments with replies", description = "Get all comments for an event including replies")
    public ResponseEntity<ApiResponse<List<EventCommentResponse>>> getEventCommentsWithReplies(
            @PathVariable Long eventId) {
        
        List<EventCommentResponse> response = eventCommentService.getEventCommentsWithReplies(eventId);
        
        return ResponseEntity.ok(new ApiResponse<>("Comments with replies retrieved successfully", "200", response));
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "Get a specific comment", description = "Get a specific comment by ID")
    public ResponseEntity<ApiResponse<EventCommentResponse>> getComment(@PathVariable Long commentId) {
        
        EventCommentResponse response = eventCommentService.getComment(commentId);
        
        return ResponseEntity.ok(new ApiResponse<>("Comment retrieved successfully", "200", response));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "Update a comment", description = "Update a comment's content")
    public ResponseEntity<ApiResponse<EventCommentResponse>> updateComment(
            @PathVariable Long commentId,
            @Parameter(description = "New comment content") @RequestParam String content,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventCommentResponse response = eventCommentService.updateComment(commentId, content, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Comment updated successfully", "200", response));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete a comment", description = "Delete a comment (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long commentId,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        eventCommentService.deleteComment(commentId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Comment deleted successfully", "200", null));
    }

    @PostMapping("/{commentId}/like")
    @Operation(summary = "Like/unlike a comment", description = "Toggle like status for a comment")
    public ResponseEntity<ApiResponse<EventCommentResponse>> toggleLike(
            @PathVariable Long commentId,
            @RequestHeader("X-USER_ID") String userContext) {
        
        String username = extractUsername(userContext);
        EventCommentResponse response = eventCommentService.toggleLike(commentId, username);
        
        return ResponseEntity.ok(new ApiResponse<>("Like toggled successfully", "200", response));
    }

    @GetMapping("/user/{username}")
    @Operation(summary = "Get user's comments", description = "Get paginated comments by a specific user")
    public ResponseEntity<ApiResponse<PagedResponse<EventCommentResponse>>> getUserComments(
            @PathVariable String username,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventCommentResponse> response = eventCommentService.getUserComments(username, page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("User comments retrieved successfully", "200", response));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent comments", description = "Get paginated recent comments")
    public ResponseEntity<ApiResponse<PagedResponse<EventCommentResponse>>> getRecentComments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventCommentResponse> response = eventCommentService.getRecentComments(page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("Recent comments retrieved successfully", "200", response));
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular comments", description = "Get paginated popular comments")
    public ResponseEntity<ApiResponse<PagedResponse<EventCommentResponse>>> getPopularComments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        
        PagedResponse<EventCommentResponse> response = eventCommentService.getPopularComments(page, size);
        
        return ResponseEntity.ok(new ApiResponse<>("Popular comments retrieved successfully", "200", response));
    }

    private String extractUsername(String userContext) {
        // This is a simplified implementation
        // In a real app, you'd decode the JWT token or user context
        return userContext; // Assuming userContext contains the username
    }
} 