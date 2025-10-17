package com.ijaa.event.service.impl;

import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.entity.EventComment;
import com.ijaa.event.domain.entity.EventCommentLike;
import com.ijaa.event.domain.entity.EventPost;
import com.ijaa.event.domain.request.EventCommentRequest;
import com.ijaa.event.domain.response.EventCommentResponse;
import com.ijaa.event.repository.EventCommentRepository;
import com.ijaa.event.repository.EventCommentLikeRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.repository.EventPostRepository;
import com.ijaa.event.service.EventCommentService;
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
    private final EventPostRepository eventPostRepository;

    @Override
    @Transactional
    public EventCommentResponse createComment(EventCommentRequest request, String username) {
        log.info("Creating comment for post: {} by user: {}", request.getPostId(), username);

        // Verify post exists
        EventPost post = eventPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        EventComment comment = new EventComment();
        comment.setPostId(request.getPostId());
        comment.setUsername(username);
        comment.setAuthorName(request.getAuthorName());
        comment.setUserId(request.getUserId());
        comment.setContent(request.getContent());
        comment.setParentCommentId(request.getParentCommentId());

        EventComment savedComment = eventCommentRepository.save(comment);
        
        // Update comment count for the post
        updatePostCommentCount(request.getPostId());
        
        return mapToResponse(savedComment, username);
    }

    @Override
    public PagedResponse<EventCommentResponse> getPostComments(Long postId, int page, int size, String currentUsername) {
        log.info("Getting comments for post: {}, page: {}, size: {}", postId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository
                .findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(postId, pageable);

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
    public List<EventCommentResponse> getPostCommentsWithReplies(Long postId, String currentUsername) {
        log.info("Getting all comments with replies for post: {}", postId);

        List<EventComment> comments = eventCommentRepository.findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(postId);
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
        
        // Update comment count for the post
        updatePostCommentCount(comment.getPostId());
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
    public PagedResponse<EventCommentResponse> getRecentCommentsByPostId(Long postId, int page, int size, String currentUsername) {
        log.info("Getting recent comments for post: {}, page: {}, size: {}", postId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findRecentCommentsByPostId(postId, pageable);

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
    public PagedResponse<EventCommentResponse> getPopularCommentsByPostId(Long postId, int page, int size, String currentUsername) {
        log.info("Getting popular comments for post: {}, page: {}, size: {}", postId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findPopularCommentsByPostId(postId, pageable);

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
        
        return new EventCommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getUsername(),
                comment.getAuthorName(), // Using stored author name from database
                comment.getUserId(), // User ID for profile photo
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

        return new EventCommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getUsername(),
                comment.getAuthorName(), // Using stored author name from database
                comment.getUserId(), // User ID for profile photo
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
     * Updates the comment count for a post by counting all non-deleted comments
     */
    private void updatePostCommentCount(Long postId) {
        try {
            Long commentCount = eventCommentRepository.countByPostId(postId);
            EventPost post = eventPostRepository.findById(postId).orElse(null);
            if (post != null) {
                post.setCommentsCount(commentCount.intValue());
                eventPostRepository.save(post);
                log.debug("Updated comment count for post {} to {}", postId, commentCount);
            }
        } catch (Exception e) {
            log.error("Failed to update comment count for post {}: {}", postId, e.getMessage());
        }
    }

    /**
     * Recalculates comment counts for all posts (useful for fixing existing data)
     */
    @Transactional
    public void recalculateAllCommentCounts() {
        log.info("Recalculating comment counts for all posts");
        try {
            List<EventPost> allPosts = eventPostRepository.findAll();
            for (EventPost post : allPosts) {
                Long commentCount = eventCommentRepository.countByPostId(post.getId());
                post.setCommentsCount(commentCount.intValue());
                eventPostRepository.save(post);
                log.debug("Updated comment count for post {} to {}", post.getId(), commentCount);
            }
            log.info("Successfully recalculated comment counts for {} posts", allPosts.size());
        } catch (Exception e) {
            log.error("Failed to recalculate comment counts: {}", e.getMessage());
        }
    }

} 
