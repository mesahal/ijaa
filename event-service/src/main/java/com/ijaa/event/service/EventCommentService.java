package com.ijaa.event.service;

import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.request.EventCommentRequest;
import com.ijaa.event.domain.response.EventCommentResponse;

import java.util.List;

public interface EventCommentService {

    // Create a new comment
    EventCommentResponse createComment(EventCommentRequest request, String username);

    // Get comments for an event (paginated)
    PagedResponse<EventCommentResponse> getEventComments(Long eventId, int page, int size, String currentUsername);

    // Get all comments for an event (with replies)
    List<EventCommentResponse> getEventCommentsWithReplies(Long eventId, String currentUsername);

    // Get a specific comment
    EventCommentResponse getComment(Long commentId, String currentUsername);

    // Update a comment
    EventCommentResponse updateComment(Long commentId, String content, String username);

    // Delete a comment (soft delete)
    void deleteComment(Long commentId, String username);

    // Like/unlike a comment
    EventCommentResponse toggleLike(Long commentId, String username);

    // Get user's comments
    PagedResponse<EventCommentResponse> getUserComments(String username, int page, int size, String currentUsername);

    // Get recent comments
    PagedResponse<EventCommentResponse> getRecentComments(int page, int size, String currentUsername);

    // Get recent comments for a specific event
    PagedResponse<EventCommentResponse> getRecentCommentsByEventId(Long eventId, int page, int size, String currentUsername);

    // Get popular comments
    PagedResponse<EventCommentResponse> getPopularComments(int page, int size, String currentUsername);

    // Get popular comments for a specific event
    PagedResponse<EventCommentResponse> getPopularCommentsByEventId(Long eventId, int page, int size, String currentUsername);
} 
