package com.ijaa.event.service;

import com.ijaa.event.domain.common.PagedResponse;
import com.ijaa.event.domain.response.EventPostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventPostService {

    // Create a new post with content and/or media files
    EventPostResponse createPostWithContentAndMedia(Long eventId, String content, List<MultipartFile> files, String username);

    // Get posts for an event (paginated)
    PagedResponse<EventPostResponse> getEventPosts(Long eventId, int page, int size, String currentUsername);

    // Get all posts for an event
    List<EventPostResponse> getAllEventPosts(Long eventId, String currentUsername);

    // Get a specific post
    EventPostResponse getPost(Long postId, String currentUsername);

    // Update a post with content and/or media files
    EventPostResponse updatePostWithContentAndMedia(Long postId, String content, List<MultipartFile> files, String username);
    
    // Update a post with content, new media files, and media files to remove
    EventPostResponse updatePostWithContentAndMedia(Long postId, String content, List<MultipartFile> files, List<String> filesToRemove, String username);

    // Remove media files from a post
    void removeMediaFromPost(Long postId, List<String> fileNames, String username);

    // Like/unlike a post
    EventPostResponse toggleLike(Long postId, String username);

    // Get user's posts
    PagedResponse<EventPostResponse> getUserPosts(String username, int page, int size, String currentUsername);

    // Get user's posts for a specific event
    PagedResponse<EventPostResponse> getUserEventPosts(String username, Long eventId, int page, int size, String currentUsername);
    
    // Delete a post
    void deletePost(Long postId, String username);

}
