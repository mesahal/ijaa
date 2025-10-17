package com.ijaa.event.service.impl;

import com.ijaa.event.common.service.BaseService;
import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.entity.EventPost;
import com.ijaa.event.domain.response.EventPostResponse;
import com.ijaa.event.domain.response.PostMediaResponse;
import com.ijaa.event.presenter.rest.client.FileServiceClient;
import com.ijaa.event.repository.EventPostRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.EventPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventPostServiceImpl extends BaseService implements EventPostService {

    private final EventPostRepository eventPostRepository;
    private final EventRepository eventRepository;
    private final FileServiceClient fileServiceClient;

    public EventPostServiceImpl(EventPostRepository eventPostRepository, 
                               EventRepository eventRepository, 
                               FileServiceClient fileServiceClient, 
                               com.fasterxml.jackson.databind.ObjectMapper objectMapper) {
        super(objectMapper);
        this.eventPostRepository = eventPostRepository;
        this.eventRepository = eventRepository;
        this.fileServiceClient = fileServiceClient;
    }

    @Override
    @Transactional
    public EventPostResponse createPostWithContentAndMedia(Long eventId, String content, List<MultipartFile> files, String username) {
        log.info("Creating post with content and/or media for event: {} by user: {}, content: {}, files: {}", 
                eventId, username, content != null ? "present" : "none", 
                files != null ? files.size() : 0);

        // Validate event exists
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        // Check if event is active
        if (!event.getActive()) {
            throw new RuntimeException("Cannot post on inactive event");
        }

        // Check if the user is the event creator
        if (!event.getCreatedByUsername().equals(username)) {
            throw new RuntimeException("Only the event creator can post on this event");
        }

        // Determine post type based on content and files
        EventPost.PostType postType = determinePostType(content, files);

        // Get user ID for the post author
        String userId = getUserIdByUsername(username);
        if (userId == null) {
            log.warn("Could not fetch user ID for username: {}, proceeding without user ID", username);
        }

        // Create the post
        EventPost post = new EventPost();
        post.setEventId(eventId);
        post.setUsername(username);
        post.setUserId(userId);
        post.setContent(content);
        post.setPostType(postType);
        post.setIsEdited(false);
        post.setIsDeleted(false);
        post.setLikes(0);
        post.setCommentsCount(0);

        EventPost savedPost = eventPostRepository.save(post);
        log.info("Post created successfully with ID: {} by event creator: {}", savedPost.getId(), username);

        // Upload media files if provided
        if (files != null && !files.isEmpty()) {
            log.info("Uploading {} media files for post: {}", files.size(), savedPost.getId());
            
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file != null && !file.isEmpty()) {
                    try {
                        // Determine media type based on file content type
                        String mediaType = determineMediaType(file.getContentType());
                        
                        // Get authorization token from current request
                        String authToken = getAuthorizationToken();
                        if (authToken == null) {
                            log.error("Authorization token not available for file upload");
                            continue; // Skip this file and continue with others
                        }
                        
                        // Upload the file using the file service client
                        fileServiceClient.uploadPostMedia(savedPost.getId().toString(), file, mediaType, authToken, username);
                        log.info("Successfully uploaded media file {} for post: {}", file.getOriginalFilename(), savedPost.getId());
                    } catch (Exception e) {
                        String errorMessage = extractErrorMessage(e);
                        log.error("Failed to upload media file {} for post: {} - Error: {}", 
                                file.getOriginalFilename(), savedPost.getId(), errorMessage);
                        // Continue with other files even if one fails
                    }
                }
            }
        }

        return mapToResponse(savedPost, username);
    }

    private EventPost.PostType determinePostType(String content, List<MultipartFile> files) {
        boolean hasContent = content != null && !content.trim().isEmpty();
        boolean hasFiles = files != null && !files.isEmpty();
        
        if (hasContent && hasFiles) {
            return EventPost.PostType.MIXED;
        } else if (hasFiles && files != null) {
            // Check if files are images or videos
            boolean hasImages = false;
            boolean hasVideos = false;
            
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String contentType = file.getContentType();
                    if (contentType != null) {
                        if (contentType.startsWith("image/")) {
                            hasImages = true;
                        } else if (contentType.startsWith("video/")) {
                            hasVideos = true;
                        }
                    }
                }
            }
            
            if (hasImages && hasVideos) {
                return EventPost.PostType.MIXED;
            } else if (hasImages) {
                return EventPost.PostType.IMAGE;
            } else if (hasVideos) {
                return EventPost.PostType.VIDEO;
            } else {
                return EventPost.PostType.IMAGE; // Default fallback
            }
        } else {
            return EventPost.PostType.TEXT;
        }
    }

    private String determineMediaType(String contentType) {
        if (contentType == null) {
            return "IMAGE"; // Default to IMAGE if content type is unknown
        }
        
        if (contentType.startsWith("image/")) {
            return "IMAGE";
        } else if (contentType.startsWith("video/")) {
            return "VIDEO";
        } else {
            // Default to IMAGE for unknown types
            return "IMAGE";
        }
    }

    @Override
    public PagedResponse<EventPostResponse> getEventPosts(Long eventId, int page, int size, String currentUsername) {
        log.info("Getting posts for event: {}, page: {}, size: {}", eventId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventPost> posts = eventPostRepository.findByEventIdAndIsDeletedFalseOrderByCreatedAtDesc(eventId, pageable);

        List<EventPostResponse> responses = posts.getContent().stream()
                .map(post -> mapToResponse(post, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }

    @Override
    public List<EventPostResponse> getAllEventPosts(Long eventId, String currentUsername) {
        log.info("Getting all posts for event: {}", eventId);

        List<EventPost> posts = eventPostRepository.findByEventIdAndIsDeletedFalseOrderByCreatedAtDesc(
                eventId, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        return posts.stream()
                .map(post -> mapToResponse(post, currentUsername))
                .collect(Collectors.toList());
    }

    @Override
    public EventPostResponse getPost(Long postId, String currentUsername) {
        log.info("Getting post: {}", postId);

        EventPost post = eventPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        return mapToResponse(post, currentUsername);
    }

    @Override
    @Transactional
    public EventPostResponse updatePost(Long postId, String content, String username) {
        log.info("Updating post: {} by user: {}", postId, username);

        EventPost post = eventPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        if (!post.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to update this post");
        }

        post.setContent(content);
        post.setIsEdited(true);

        EventPost updatedPost = eventPostRepository.save(post);
        log.info("Post updated successfully: {}", postId);

        return mapToResponse(updatedPost, username);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, String username) {
        log.info("Deleting post: {} by user: {}", postId, username);

        EventPost post = eventPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has already been deleted");
        }

        if (!post.getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }

        post.setIsDeleted(true);
        eventPostRepository.save(post);
        log.info("Post soft deleted successfully: {}", postId);
    }

    @Override
    @Transactional
    public EventPostResponse toggleLike(Long postId, String username) {
        log.info("Toggling like for post: {} by user: {}", postId, username);

        EventPost post = eventPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post has been deleted");
        }

        // For now, we'll just increment/decrement likes
        // In a real implementation, you'd have a separate likes table
        post.setLikes(post.getLikes() + 1);
        EventPost updatedPost = eventPostRepository.save(post);

        log.info("Post like toggled successfully: {}", postId);
        return mapToResponse(updatedPost, username);
    }

    @Override
    public PagedResponse<EventPostResponse> getUserPosts(String username, int page, int size, String currentUsername) {
        log.info("Getting posts by user: {}, page: {}, size: {}", username, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventPost> posts = eventPostRepository.findByUsernameAndIsDeletedFalseOrderByCreatedAtDesc(username, pageable);

        List<EventPostResponse> responses = posts.getContent().stream()
                .map(post -> mapToResponse(post, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }

    @Override
    public PagedResponse<EventPostResponse> getUserEventPosts(String username, Long eventId, int page, int size, String currentUsername) {
        log.info("Getting posts by user: {} for event: {}, page: {}, size: {}", username, eventId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventPost> posts = eventPostRepository.findByUsernameAndEventIdAndIsDeletedFalseOrderByCreatedAtDesc(username, eventId, pageable);

        List<EventPostResponse> responses = posts.getContent().stream()
                .map(post -> mapToResponse(post, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                responses,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }








    private EventPostResponse mapToResponse(EventPost post, String currentUsername) {
        // Fetch media files for this post
        List<PostMediaResponse> mediaFiles = getPostMediaFiles(post.getId().toString());
        
        // Fetch the actual creator name
        String creatorName = getEventCreatorName(post.getEventId());
        
        // Use stored userId, fallback to fetching if not available
        String userId = post.getUserId();
        if (userId == null) {
            log.debug("No stored userId for post {}, attempting to fetch from user service", post.getId());
            userId = getUserIdByUsername(post.getUsername());
        }
        
        return EventPostResponse.builder()
                .id(post.getId())
                .eventId(post.getEventId())
                .username(post.getUsername())
                .userId(userId)
                .content(post.getContent())
                .postType(post.getPostType())
                .isEdited(post.getIsEdited())
                .isDeleted(post.getIsDeleted())
                .likes(post.getLikes())
                .commentsCount(post.getCommentsCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .isLikedByUser(false) // TODO: Implement actual like status check
                .mediaFiles(mediaFiles)
                .recentComments(List.of()) // TODO: Implement recent comments retrieval
                .creatorName(creatorName)
                .build();
    }

    private String getEventCreatorName(Long eventId) {
        try {
            Event event = eventRepository.findById(eventId).orElse(null);
            if (event != null && event.getCreatedByUsername() != null) {
                // For now, return the actual name since we know the current user is the creator
                // TODO: Implement proper user service call to get the actual name
                return "Md Sahal";
            }
        } catch (Exception e) {
            log.warn("Failed to fetch event creator name for event: {} - Error: {}", eventId, e.getMessage());
        }
        return "Unknown Creator";
    }

    private String getUserIdByUsername(String username) {
        try {
            log.info("Getting current user ID from JWT token for username: {}", username);
            // Get the current user ID directly from the JWT token
            String userId = getCurrentUserId();
            log.info("Extracted user ID from JWT: {}", userId);
            if (userId != null) {
                log.info("Successfully extracted user ID: {} for username: {}", userId, username);
                return userId;
            } else {
                log.warn("No user ID found in JWT token for username: {}", username);
            }
        } catch (Exception e) {
            log.warn("Failed to get user ID from JWT token for username: {} - Error: {}", username, e.getMessage());
        }
        return null;
    }


    private List<PostMediaResponse> getPostMediaFiles(String postId) {
        try {
            log.debug("Fetching media files for post: {}", postId);
            // The getAllPostMedia endpoint is public and doesn't require authentication
            return fileServiceClient.getPostMedia(postId);
        } catch (Exception e) {
            log.warn("Failed to fetch media files for post: {} - Error: {}", postId, e.getMessage());
            return new ArrayList<>();
        }
    }

    private String getAuthorizationToken() {
        try {
            org.springframework.web.context.request.RequestAttributes requestAttributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                jakarta.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes) requestAttributes).getRequest();
                String authHeader = request.getHeader("Authorization");
                return authHeader != null ? authHeader : null;
            }
        } catch (Exception e) {
            log.warn("Failed to get authorization token from request: {}", e.getMessage());
        }
        return null;
    }

    private String extractErrorMessage(Exception e) {
        String message = e.getMessage();
        
        // Check for file size errors
        if (message != null) {
            if (message.contains("File size exceeds maximum allowed size")) {
                if (message.contains("5MB")) {
                    return "Image file size exceeds the maximum allowed limit of 5MB. Please choose a smaller image.";
                } else if (message.contains("50MB")) {
                    return "Video file size exceeds the maximum allowed limit of 50MB. Please choose a smaller video.";
                } else {
                    return "File size exceeds the maximum allowed limit. Images: 5MB max, Videos: 50MB max.";
                }
            }
            
            if (message.contains("Invalid image type") || message.contains("Invalid video type")) {
                return "Invalid file type. Allowed image types: JPG, JPEG, PNG, WebP. Allowed video types: MP4, AVI, MOV, WMV, FLV, WebM.";
            }
            
            if (message.contains("File is empty")) {
                return "The selected file is empty. Please choose a valid file.";
            }
            
            if (message.contains("Required request header 'X-Username'")) {
                return "Authentication error. Please try again.";
            }
        }
        
        return message != null ? message : "Unknown error occurred during file upload";
    }
}
