package com.ijaa.file.controller;

import com.ijaa.file.common.annotation.RequiresFeature;
import com.ijaa.file.common.utils.AppUtils;
import com.ijaa.file.domain.dto.FileUploadResponse;
import com.ijaa.file.domain.dto.PhotoUrlResponse;
import com.ijaa.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(AppUtils.BASE_URL + "/posts")
@RequiredArgsConstructor
@Tag(name = "Event Post Media", description = "APIs for managing event post media files (images and videos)")
@SecurityRequirement(name = "bearerAuth")
public class EventPostMediaController {

    private final FileService fileService;

    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Test endpoint to verify controller is working")
    public ResponseEntity<String> test() {
        log.info("Test endpoint called - EventPostMediaController is working");
        return ResponseEntity.ok("EventPostMediaController is working");
    }

    @PostMapping("/{postId}/media")
    @RequiresFeature("events.posts.media")
    @Operation(
        summary = "Upload post media", 
        description = "Upload an image or video file for a post. Supports common image formats (JPEG, PNG, GIF) and video formats (MP4, AVI, MOV)."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Media uploaded successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FileUploadResponse.class),
                examples = @ExampleObject(
                    value = """
                    {
                      "message": "Post media uploaded successfully",
                      "fileUrl": "/ijaa/api/v1/files/posts/1/media/image.jpg",
                      "fileName": "image.jpg",
                      "fileSize": 1024000
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid file or upload failed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Feature not enabled")
    })
    public ResponseEntity<FileUploadResponse> uploadPostMedia(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable String postId,
            @Parameter(description = "Media file to upload", required = true)
            @RequestParam("file") MultipartFile file,
            @Parameter(description = "Media type", required = true, example = "IMAGE", schema = @Schema(allowableValues = {"IMAGE", "VIDEO"}))
            @RequestParam("mediaType") String mediaType,
            @Parameter(description = "Username of the uploader")
            @RequestHeader("X-Username") String username) {
        
        log.info("Upload post media request received - PostId: {}, FileName: {}, MediaType: {}, Username: {}", 
                postId, file.getOriginalFilename(), mediaType, username);
        
        log.info("Uploading post media for post: {} by user: {}, type: {}", postId, username, mediaType);
        
        try {
            FileUploadResponse response = fileService.uploadPostMedia(postId, file, mediaType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error uploading post media: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(
                new FileUploadResponse("Failed to upload post media: " + e.getMessage(), null, null, 0L)
            );
        }
    }

    @GetMapping("/{postId}/media")
    @Operation(
        summary = "Get all post media", 
        description = "Get all media files for a post, ordered by file order"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post media retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ijaa.file.domain.dto.PostMediaResponse.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Post not found or no media files")
    })
    public ResponseEntity<List<com.ijaa.file.domain.dto.PostMediaResponse>> getAllPostMedia(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable String postId) {
        
        log.info("Get all post media request received - PostId: {}", postId);
        
        try {
            List<com.ijaa.file.domain.dto.PostMediaResponse> response = fileService.getAllPostMedia(postId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting all post media: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{postId}/media/{fileName}")
    @Operation(
        summary = "Get post media URL", 
        description = "Get the URL for a post media file"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Media URL retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PhotoUrlResponse.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    public ResponseEntity<PhotoUrlResponse> getPostMediaUrl(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable String postId,
            @Parameter(description = "File name", required = true, example = "image.jpg")
            @PathVariable String fileName) {
        
        log.info("Getting post media URL for post: {}, file: {}", postId, fileName);
        
        try {
            PhotoUrlResponse response = fileService.getPostMediaUrl(postId, fileName);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error getting post media URL: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{postId}/media/file/{fileName}")
    @Operation(
        summary = "Serve post media file", 
        description = "Serve the actual post media file (public endpoint). Returns the file with appropriate content type."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Media file served successfully",
            content = @Content(
                mediaType = "application/octet-stream",
                schema = @Schema(type = "string", format = "binary")
            )
        ),
        @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    public ResponseEntity<Resource> getPostMediaFile(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable String postId,
            @Parameter(description = "File name", required = true, example = "image.jpg")
            @PathVariable String fileName,
            HttpServletRequest request) {
        
        log.info("Serving post media file for post: {}, file: {}", postId, fileName);
        
        try {
            Resource resource = fileService.getPostMediaFile(postId, fileName);
            
            // Determine content type
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error serving post media file: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{postId}/media/{fileName}")
    @Operation(
        summary = "Delete post media", 
        description = "Delete a specific post media file. Only the post author can delete media files."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Media file deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or deletion failed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Media file not found")
    })
    public ResponseEntity<Void> deletePostMedia(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable String postId,
            @Parameter(description = "File name", required = true, example = "image.jpg")
            @PathVariable String fileName,
            @Parameter(description = "Username of the deleter") 
            @RequestHeader("X-Username") String username) {
        
        log.info("Deleting post media for post: {}, file: {} by user: {}", postId, fileName, username);
        
        try {
            fileService.deletePostMedia(postId, fileName);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting post media: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{postId}/media")
    @Operation(
        summary = "Delete all post media", 
        description = "Delete all media files for a post. Only the post author can delete all media files."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "All media files deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request or deletion failed"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    public ResponseEntity<Void> deleteAllPostMedia(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable String postId,
            @Parameter(description = "Username of the deleter") 
            @RequestHeader("X-Username") String username) {
        
        log.info("Deleting all post media for post: {} by user: {}", postId, username);
        
        try {
            fileService.deleteAllPostMedia(postId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting all post media: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
