package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.entity.EventComment;
import com.ijaa.event_service.domain.request.EventCommentRequest;
import com.ijaa.event_service.domain.response.EventCommentResponse;
import com.ijaa.event_service.repository.EventCommentRepository;
import com.ijaa.event_service.repository.EventRepository;
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
    private final EventRepository eventRepository;

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
        return mapToResponse(savedComment);
    }

    @Override
    public PagedResponse<EventCommentResponse> getEventComments(Long eventId, int page, int size) {
        log.info("Getting comments for event: {}, page: {}, size: {}", eventId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository
                .findByEventIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(eventId, pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(this::mapToResponse)
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
    public List<EventCommentResponse> getEventCommentsWithReplies(Long eventId) {
        log.info("Getting all comments with replies for event: {}", eventId);

        List<EventComment> comments = eventCommentRepository.findByEventIdAndIsDeletedFalseOrderByCreatedAtAsc(eventId);
        return comments.stream()
                .map(this::mapToResponseWithReplies)
                .collect(Collectors.toList());
    }

    @Override
    public EventCommentResponse getComment(Long commentId) {
        log.info("Getting comment: {}", commentId);

        EventComment comment = eventCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getIsDeleted()) {
            throw new RuntimeException("Comment has been deleted");
        }

        return mapToResponseWithReplies(comment);
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
        return mapToResponse(updatedComment);
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

        // Simple like toggle (in a real app, you'd have a separate likes table)
        comment.setLikes(comment.getLikes() + 1);

        EventComment updatedComment = eventCommentRepository.save(comment);
        return mapToResponse(updatedComment);
    }

    @Override
    public PagedResponse<EventCommentResponse> getUserComments(String username, int page, int size) {
        log.info("Getting comments by user: {}, page: {}, size: {}", username, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository
                .findByUsernameAndIsDeletedFalseOrderByCreatedAtDesc(username, pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(this::mapToResponse)
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
    public PagedResponse<EventCommentResponse> getRecentComments(int page, int size) {
        log.info("Getting recent comments, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findRecentComments(pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(this::mapToResponse)
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
    public PagedResponse<EventCommentResponse> getPopularComments(int page, int size) {
        log.info("Getting popular comments, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventComment> comments = eventCommentRepository.findPopularComments(pageable);

        List<EventCommentResponse> responses = comments.getContent().stream()
                .map(this::mapToResponse)
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

    private EventCommentResponse mapToResponse(EventComment comment) {
        return new EventCommentResponse(
                comment.getId(),
                comment.getEventId(),
                comment.getUsername(),
                comment.getContent(),
                comment.getIsEdited(),
                comment.getIsDeleted(),
                comment.getParentCommentId(),
                comment.getLikes(),
                comment.getReplies(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                null // replies will be loaded separately
        );
    }

    private EventCommentResponse mapToResponseWithReplies(EventComment comment) {
        List<EventCommentResponse> replies = null;
        if (comment.getParentCommentId() == null) {
            // Only load replies for top-level comments
            replies = eventCommentRepository.findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(comment.getId())
                    .stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
        }

        return new EventCommentResponse(
                comment.getId(),
                comment.getEventId(),
                comment.getUsername(),
                comment.getContent(),
                comment.getIsEdited(),
                comment.getIsDeleted(),
                comment.getParentCommentId(),
                comment.getLikes(),
                comment.getReplies(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                replies
        );
    }
} 