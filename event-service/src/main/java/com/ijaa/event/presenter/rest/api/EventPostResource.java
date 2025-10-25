package com.ijaa.event.presenter.rest.api;

import com.ijaa.event.common.annotation.RequiresFeature;
import com.ijaa.event.common.annotation.RequiresRole;
import com.ijaa.event.common.service.BaseService;
import com.ijaa.event.common.utils.AppUtils;
import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.response.EventPostResponse;
import com.ijaa.event.service.EventPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(AppUtils.BASE_URL + "/posts")
@Tag(name = "Event Posts", description = "APIs for managing event posts and discussions")
@SecurityRequirement(name = "bearerAuth")
public class EventPostResource extends BaseService {

    private final EventPostService eventPostService;

    public EventPostResource(EventPostService eventPostService, com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventPostService = eventPostService;
    }

    @PostMapping(consumes = "multipart/form-data")
    @RequiresRole("USER")
    @RequiresFeature("events.posts.create")
    @Operation(
        summary = "Create a new post", 
        description = "Create a new post in an event's discussion section. Accepts text content and/or media files (images/videos). At least one of text content or media files must be provided. File size limits: Images (5MB max), Videos (50MB max). Supported formats: Images (JPG, JPEG, PNG, WebP), Videos (MP4, AVI, MOV, WMV, FLV, WebM)."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201", 
            description = "Post created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventPostResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "id": 1,
                      "eventId": 13,
                      "username": "mdsahal.info@gmail.com",
                      "content": "This is a great event!",
                      "postType": "MIXED",
                      "isEdited": false,
                      "isDeleted": false,
                      "likes": 0,
                      "commentsCount": 0,
                      "createdAt": "2024-01-15T10:30:00",
                      "updatedAt": "2024-01-15T10:30:00",
                      "mediaFiles": [
                        {
                          "id": 1,
                          "fileName": "photo1.jpg",
                          "fileUrl": "/ijaa/api/v1/files/posts/1/media/photo1.jpg",
                          "fileType": "image/jpeg",
                          "mediaType": "IMAGE",
                          "fileSize": 1024000,
                          "fileOrder": 0
                        }
                      ],
                      "isLikedByUser": false,
                      "recentComments": []
                    }
                    """
                )
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data - at least text content or media files must be provided"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Feature not enabled")
    })
    public ResponseEntity<EventPostResponse> createPost(
            @Parameter(description = "Event ID", required = true, example = "13")
            @RequestParam("eventId") Long eventId,
            @Parameter(description = "Text content of the post", example = "This is a great event!")
            @RequestParam(value = "content", required = false) String content,
            @Parameter(description = "Media files to attach to the post", required = false)
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Validate that at least content or files are provided
        if ((content == null || content.trim().isEmpty()) && (files == null || files.isEmpty())) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Creating post for event: {} by user: {}, content: {}, files: {}", 
                eventId, username, content != null ? "present" : "none", 
                files != null ? files.size() : 0);
        
        try {
            EventPostResponse response = eventPostService.createPostWithContentAndMedia(eventId, content, files, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating post: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/event/{eventId}")
    @RequiresRole("USER")
    @RequiresFeature("events.posts")
    @Operation(
        summary = "Get posts for an event", 
        description = "Get paginated posts for a specific event, ordered by creation date (newest first)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Posts retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Feature not enabled")
    })
    public ResponseEntity<PagedResponse<EventPostResponse>> getEventPosts(
            @Parameter(description = "Event ID", required = true, example = "13")
            @PathVariable Long eventId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Getting posts for event: {}, page: {}, size: {}", eventId, page, size);
        PagedResponse<EventPostResponse> response = eventPostService.getEventPosts(eventId, page, size, currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{eventId}/all")
    @Operation(
        summary = "Get all posts for an event", 
        description = "Get all posts for a specific event (non-paginated), ordered by creation date (newest first)"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "All posts retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventPostResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<EventPostResponse>> getAllEventPosts(
            @Parameter(description = "Event ID", required = true, example = "13")
            @PathVariable Long eventId) {
        
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Getting all posts for event: {}", eventId);
        List<EventPostResponse> response = eventPostService.getAllEventPosts(eventId, currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    @Operation(
        summary = "Get a specific post", 
        description = "Get details of a specific post including media files and recent comments"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Post retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventPostResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<EventPostResponse> getPost(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long postId) {
        
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Getting post: {}", postId);
        EventPostResponse response = eventPostService.getPost(postId, currentUsername);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{postId}", consumes = {"multipart/form-data"})
    @RequiresRole("USER")
    @RequiresFeature("events.posts.update")
    @Operation(
        summary = "Update a post", 
        description = "Update the content, add new media files, and/or remove existing media files from a post. " +
                     "Only the post author can update their post. To update media files, use multipart/form-data. " +
                     "To update only text content, use application/json. To remove media files, specify their names in the 'removeMedia' parameter."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Post updated successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventPostResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to update this post"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<EventPostResponse> updatePost(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long postId,
            @Parameter(description = "New content for the post (can be null if only updating media)", 
                      example = "Updated post content")
            @RequestParam(value = "content", required = false) String content,
            @Parameter(description = "New media files to add to the post")
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @Parameter(description = "Comma-separated list of media file names to remove from the post")
            @RequestParam(value = "removeMedia", required = false) String removeMedia) {
        
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Validate that at least content, files, or files to remove are provided
        if ((content == null || content.trim().isEmpty()) && 
            (files == null || files.isEmpty()) && 
            (removeMedia == null || removeMedia.trim().isEmpty())) {
            return ResponseEntity.badRequest().build();
        }
        
        log.info("Updating post: {} by user: {}, content: {}, files: {}, removeMedia: {}", 
                postId, username, 
                content != null ? "present" : "none", 
                files != null ? files.size() : 0,
                removeMedia != null ? removeMedia : "none");
        
        try {
            // Parse comma-separated file names to remove
            List<String> filesToRemove = new ArrayList<>();
            if (removeMedia != null && !removeMedia.trim().isEmpty()) {
                filesToRemove = Arrays.stream(removeMedia.split(","))
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .collect(Collectors.toList());
            }
            
            EventPostResponse response = eventPostService.updatePostWithContentAndMedia(
                    postId, content, files, filesToRemove, username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Failed to update post: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{postId}")
    @RequiresRole("USER")
    @RequiresFeature("events.posts.delete")
    @Operation(
        summary = "Delete a post", 
        description = "Soft delete a post. Only the post author can delete their post."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Post deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Not authorized to delete this post"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long postId) {
        
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Deleting post: {} by user: {}", postId, username);
        eventPostService.deletePost(postId, username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    @RequiresRole("USER")
    @RequiresFeature("events.posts.like")
    @Operation(
        summary = "Like/Unlike a post", 
        description = "Toggle like status for a post. If the user has already liked the post, it will be unliked."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "Like status toggled successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = EventPostResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<EventPostResponse> toggleLike(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long postId) {
        
        String username = getCurrentUsername();
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Toggling like for post: {} by user: {}", postId, username);
        EventPostResponse response = eventPostService.toggleLike(postId, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}")
    @Operation(
        summary = "Get user's posts", 
        description = "Get paginated posts created by a specific user across all events"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "User posts retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PagedResponse<EventPostResponse>> getUserPosts(
            @Parameter(description = "Username", required = true, example = "mdsahal.info@gmail.com")
            @PathVariable String username,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Getting posts by user: {}, page: {}, size: {}", username, page, size);
        PagedResponse<EventPostResponse> response = eventPostService.getUserPosts(username, page, size, currentUsername);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}/event/{eventId}")
    @Operation(
        summary = "Get user's posts for an event", 
        description = "Get paginated posts created by a specific user for a specific event"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200", 
            description = "User event posts retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PagedResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<PagedResponse<EventPostResponse>> getUserEventPosts(
            @Parameter(description = "Username", required = true, example = "mdsahal.info@gmail.com")
            @PathVariable String username,
            @Parameter(description = "Event ID", required = true, example = "13")
            @PathVariable Long eventId,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        String currentUsername = getCurrentUsername();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        log.info("Getting posts by user: {} for event: {}, page: {}, size: {}", username, eventId, page, size);
        PagedResponse<EventPostResponse> response = eventPostService.getUserEventPosts(username, eventId, page, size, currentUsername);
        return ResponseEntity.ok(response);
    }







}
