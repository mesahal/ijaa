package com.ijaa.event_service.service;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.request.EventCommentRequest;
import com.ijaa.event_service.domain.response.EventCommentResponse;

import java.util.List;

public interface EventCommentService {

    // Create a new comment
    EventCommentResponse createComment(EventCommentRequest request, String username);

    // Get comments for an event (paginated)
    PagedResponse<EventCommentResponse> getEventComments(Long eventId, int page, int size);

    // Get all comments for an event (with replies)
    List<EventCommentResponse> getEventCommentsWithReplies(Long eventId);

    // Get a specific comment
    EventCommentResponse getComment(Long commentId);

    // Update a comment
    EventCommentResponse updateComment(Long commentId, String content, String username);

    // Delete a comment (soft delete)
    void deleteComment(Long commentId, String username);

    // Like/unlike a comment
    EventCommentResponse toggleLike(Long commentId, String username);

    // Get user's comments
    PagedResponse<EventCommentResponse> getUserComments(String username, int page, int size);

    // Get recent comments
    PagedResponse<EventCommentResponse> getRecentComments(int page, int size);

    // Get popular comments
    PagedResponse<EventCommentResponse> getPopularComments(int page, int size);
} 