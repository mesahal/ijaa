package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.entity.EventComment;
import com.ijaa.event_service.domain.entity.EventCommentLike;
import com.ijaa.event_service.domain.request.EventCommentRequest;
import com.ijaa.event_service.domain.response.EventCommentResponse;
import com.ijaa.event_service.repository.EventCommentRepository;
import com.ijaa.event_service.repository.EventCommentLikeRepository;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.presenter.rest.client.UserServiceClient;
import com.ijaa.event_service.domain.dto.ProfileDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ijaa.event_service.service.EventCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCommentServiceImpl implements EventCommentService {

    private final EventCommentRepository eventCommentRepository;
    private final EventCommentLikeRepository eventCommentLikeRepository;
    private final EventRepository eventRepository;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public EventCommentResponse createComment(EventCommentRequest request, String username) {
        log.info("Creating comment for event: {} by user: {}", request.getEventId(), username);

        // Verify event exists
        if (!eventRepository.existsById(request.getEventId())) {
            throw new RuntimeException("Event not found");
        }

        EventComment comment = new EventComment();
        comment.setEventId(request.getEventId());
        comment.setUsername(username);
        comment.setContent(request.getContent());
        comment.setParentCommentId(request.getParentCommentId());

        EventComment savedComment = eventCommentRepository.save(comment);
        return mapToResponse(savedComment, username);
    }

    @Override
    public PagedResponse<EventCommentResponse> getEventComments(Long eventId, int page, int size, String currentUsername) {
        log.info("Getting comments for event: {}, page: {}, size: {}", eventId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository
                .findByEventIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(eventId, pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(comment -> mapToResponse(comment, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<EventCommentResponse>(
                responses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    @Override
    public List<EventCommentResponse> getEventCommentsWithReplies(Long eventId, String currentUsername) {
        log.info("Getting all comments with replies for event: {}", eventId);

        List<EventComment> comments = eventCommentRepository.findByEventIdAndIsDeletedFalseOrderByCreatedAtAsc(eventId);
        return comments.stream()
                .map(comment -> mapToResponseWithReplies(comment, currentUsername))
                .collect(Collectors.toList());
    }

    @Override
    public EventCommentResponse getComment(Long commentId, String currentUsername) {
        log.info("Getting comment: {}", commentId);

        EventComment comment = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getIsDeleted()) {
            throw new RuntimeException("Comment has been deleted");
        }

        return mapToResponseWithReplies(comment, currentUsername);
    }

    @Override
    @Transactional
    public EventCommentResponse updateComment(Long commentId, String content, String username) {
        log.info("Updating comment: {} by user: {}", commentId, username);

        EventComment comment = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getIsDeleted()) {
            throw new RuntimeException("Comment has been deleted");
        }

        if (!comment.getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to update this comment");
        }

        comment.setContent(content);
        comment.setIsEdited(true);

        EventComment updatedComment = eventCommentRepository.save(comment);
        return mapToResponse(updatedComment, username);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String username) {
        log.info("Deleting comment: {} by user: {}", commentId, username);

        EventComment comment = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this comment");
        }

        comment.setIsDeleted(true);
        eventCommentRepository.save(comment);
    }

    @Override
    @Transactional
    public EventCommentResponse toggleLike(Long commentId, String username) {
        log.info("Toggling like for comment: {} by user: {}", commentId, username);

        EventComment comment = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getIsDeleted()) {
            throw new RuntimeException("Comment has been deleted");
        }

        // Check if user has already liked this comment
        boolean hasLiked = eventCommentLikeRepository.existsByCommentIdAndUsername(commentId, username);
        
        if (hasLiked) {
            // Unlike: remove the like record
            eventCommentLikeRepository.deleteByCommentIdAndUsername(commentId, username);
            log.info("User {} unliked comment {}", username, commentId);
        } else {
            // Like: create a new like record
            EventCommentLike like = new EventCommentLike();
            like.setCommentId(commentId);
            like.setUsername(username);
            eventCommentLikeRepository.save(like);
            log.info("User {} liked comment {}", username, commentId);
        }

        // Get updated like count
        Long likeCount = eventCommentLikeRepository.countByCommentId(commentId);
        comment.setLikes(likeCount.intValue());
        EventComment updatedComment = eventCommentRepository.save(comment);
        
        return mapToResponse(updatedComment, username);
    }

    @Override
    public PagedResponse<EventCommentResponse> getUserComments(String username, int page, int size, String currentUsername) {
        log.info("Getting comments by user: {}, page: {}, size: {}", username, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository
                .findByUsernameAndIsDeletedFalseOrderByCreatedAtDesc(username, pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(comment -> mapToResponse(comment, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<EventCommentResponse>(
                responses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    @Override
    public PagedResponse<EventCommentResponse> getRecentComments(int page, int size, String currentUsername) {
        log.info("Getting recent comments, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findRecentComments(pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(comment -> mapToResponse(comment, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<EventCommentResponse>(
                responses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    @Override
    public PagedResponse<EventCommentResponse> getPopularComments(int page, int size, String currentUsername) {
        log.info("Getting popular comments, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findPopularComments(pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(comment -> mapToResponse(comment, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<EventCommentResponse>(
                responses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    @Override
    public PagedResponse<EventCommentResponse> getRecentCommentsByEventId(Long eventId, int page, int size, String currentUsername) {
        log.info("Getting recent comments for event: {}, page: {}, size: {}", eventId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findRecentCommentsByEventId(eventId, pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(comment -> mapToResponse(comment, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<EventCommentResponse>(
                responses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    @Override
    public PagedResponse<EventCommentResponse> getPopularCommentsByEventId(Long eventId, int page, int size, String currentUsername) {
        log.info("Getting popular comments for event: {}, page: {}, size: {}", eventId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findPopularCommentsByEventId(eventId, pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(comment -> mapToResponse(comment, currentUsername))
                .collect(Collectors.toList());

        return new PagedResponse<EventCommentResponse>(
                responses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    private EventCommentResponse mapToResponse(EventComment comment, String currentUsername) {
        // Check if current user has liked this comment
        boolean isLikedByCurrentUser = false;
        if (currentUsername != null) {
            isLikedByCurrentUser = eventCommentLikeRepository.existsByCommentIdAndUsername(comment.getId(), currentUsername);
        }
        
        // Get the author's actual name from user service
        String authorName = getAuthorName(comment.getUsername());
        
        return new EventCommentResponse(
                comment.getId(),
                comment.getEventId(),
                comment.getUsername(),
                authorName, // Now using actual user name from user service
                comment.getContent(),
                comment.getIsEdited(),
                comment.getIsDeleted(),
                comment.getParentCommentId(),
                comment.getLikes(),
                comment.getReplies(),
                isLikedByCurrentUser,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                null // replies will be loaded separately
        );
    }

    private EventCommentResponse mapToResponseWithReplies(EventComment comment, String currentUsername) {
        List<EventCommentResponse> replies = null;
        if (comment.getParentCommentId() == null) {
            // Only load replies for top-level comments
            replies = eventCommentRepository.findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(comment.getId())
                    .stream()
                    .map(reply -> mapToResponse(reply, currentUsername))
                    .collect(Collectors.toList());
        }

        // Check if current user has liked this comment
        boolean isLikedByCurrentUser = false;
        if (currentUsername != null) {
            isLikedByCurrentUser = eventCommentLikeRepository.existsByCommentIdAndUsername(comment.getId(), currentUsername);
        }

        // Get the author's actual name from user service
        String authorName = getAuthorName(comment.getUsername());

        return new EventCommentResponse(
                comment.getId(),
                comment.getEventId(),
                comment.getUsername(),
                authorName, // Now using actual user name from user service
                comment.getContent(),
                comment.getIsEdited(),
                comment.getIsDeleted(),
                comment.getParentCommentId(),
                comment.getLikes(),
                comment.getReplies(),
                isLikedByCurrentUser,
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replies
        );
    }

    /**
     * Get the author's actual name from user service
     * Falls back to username if user service is unavailable or user not found
     */
    private String getAuthorName(String username) {
        try {
            // Get the Authorization header from the current request
            String authToken = getAuthorizationToken();
            if (authToken != null) {
                ProfileDto profile = userServiceClient.getProfileByUsername(username, authToken);
                return profile.getName();
            } else {
                log.warn("No authorization token available for user profile lookup");
                return username; // Fallback to username
            }
        } catch (Exception e) {
            log.warn("Failed to get user profile for username: {}. Using username as fallback. Error: {}", username, e.getMessage());
            return username; // Fallback to username if user service is unavailable
        }
    }

    /**
     * Get the Authorization token from the current request
     */
    private String getAuthorizationToken() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader("Authorization");
            return authHeader != null ? authHeader : null;
        } catch (Exception e) {
            log.warn("Failed to get authorization token from request: {}", e.getMessage());
            return null;
        }
    }
} 