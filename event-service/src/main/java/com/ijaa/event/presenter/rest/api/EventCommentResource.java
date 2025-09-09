package com.ijaa.event.presenter.rest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.event.common.annotation.RequiresFeature;
import com.ijaa.event.common.utils.AppUtils;
import com.ijaa.event.common.service.BaseService;
import com.ijaa.event.domain.common.ApiResponse;
import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.request.EventCommentRequest;
import com.ijaa.event.domain.response.EventCommentResponse;
import com.ijaa.event.service.EventCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppUtils.BASE_URL + "/comments")
@Slf4j
@Tag(name = "Event Comments")
public class EventCommentResource extends BaseService {

    private final EventCommentService eventCommentService;

    public EventCommentResource(EventCommentService eventCommentService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventCommentService = eventCommentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Add Event Comment",
        description = "Add a comment to an event (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Comment created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class),
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
    public ResponseEntity<ApiResponse<EventCommentResponse>> createComment(@Valid @RequestBody EventCommentRequest request) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        EventCommentResponse comment = eventCommentService.createComment(request, username);
        return ResponseEntity.status(201).body(new ApiResponse<>("Comment created successfully", "201", comment));
    }

    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Get Event Comments",
        description = "Get all comments for a specific event with pagination and threading support"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Event comments retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventCommentResponse>>> getEventComments(@PathVariable Long eventId) {
        // Get all comments with nested replies for the event
        String currentUsername = getCurrentUsername();
        List<EventCommentResponse> comments = eventCommentService.getEventCommentsWithReplies(eventId, currentUsername);
        return ResponseEntity.ok(new ApiResponse<>("Event comments retrieved successfully", "200", comments));
    }



    @GetMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Get Comment by ID",
        description = "Get a specific comment by ID with its replies"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Comment retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Comment not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Comment not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventCommentResponse>> getCommentById(@PathVariable Long commentId) {
        String currentUsername = getCurrentUsername();
        EventCommentResponse comment = eventCommentService.getComment(commentId, currentUsername);
        return ResponseEntity.ok(new ApiResponse<>("Comment retrieved successfully", "200", comment));
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Update Comment",
        description = "Update an existing comment (only comment author can update) (USER role required)",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Updated comment content",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventCommentRequest.class)
            )
        )
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Comment updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Not the comment author",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Forbidden",
                        value = """
                            {
                                "message": "You can only update your own comments",
                                "code": "403",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Comment not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Comment not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventCommentResponse>> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody EventCommentRequest request) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        EventCommentResponse comment = eventCommentService.updateComment(commentId, request.getContent(), username);
        return ResponseEntity.ok(new ApiResponse<>("Comment updated successfully", "200", comment));
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Delete Comment",
        description = "Delete a comment (only comment author or event organizer can delete) (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Comment deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden - Insufficient permissions",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Forbidden",
                        value = """
                            {
                                "message": "You don't have permission to delete this comment",
                                "code": "403",
                                "data": null
                            }
                            """
                    )
                }
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Comment not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Comment not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        eventCommentService.deleteComment(commentId, username);
        return ResponseEntity.ok(new ApiResponse<>("Comment deleted successfully", "200", null));
    }

    @PostMapping("/{commentId}/like")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Like/Unlike Comment",
        description = "Toggle like status for a comment (USER role required)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Comment like status updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Comment not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not Found",
                        value = """
                            {
                                "message": "Comment not found",
                                "code": "404",
                                "data": null
                            }
                            """
                    )
                }
            )
        )
    })
    public ResponseEntity<ApiResponse<EventCommentResponse>> toggleCommentLike(@PathVariable Long commentId) {
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>("Authentication required", "401", null));
        }
        EventCommentResponse comment = eventCommentService.toggleLike(commentId, username);
        return ResponseEntity.ok(new ApiResponse<>("Comment like status updated successfully", "200", comment));
    }



    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Get Recent Comments",
        description = "Get the most recent comments for a specific event"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Recent comments retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventCommentResponse>>> getRecentComments(@RequestParam Long eventId) {
        // Default pagination parameters
        int page = 0;
        int size = 10;
        String currentUsername = getCurrentUsername();
        PagedResponse<EventCommentResponse> pagedComments = eventCommentService.getRecentCommentsByEventId(eventId, page, size, currentUsername);
        return ResponseEntity.ok(new ApiResponse<>("Recent comments retrieved successfully", "200", pagedComments.getContent()));
    }

    @GetMapping("/popular")
    @PreAuthorize("hasRole('USER')")
    @RequiresFeature("events.comments")
    @Operation(
        summary = "Get Popular Comments",
        description = "Get the most popular comments (most liked) for a specific event"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Popular comments retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.event.domain.common.ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<EventCommentResponse>>> getPopularComments(@RequestParam Long eventId) {
        // Default pagination parameters
        int page = 0;
        int size = 10;
        String currentUsername = getCurrentUsername();
        PagedResponse<EventCommentResponse> pagedComments = eventCommentService.getPopularCommentsByEventId(eventId, page, size, currentUsername);
        return ResponseEntity.ok(new ApiResponse<>("Popular comments retrieved successfully", "200", pagedComments.getContent()));
    }
} 
