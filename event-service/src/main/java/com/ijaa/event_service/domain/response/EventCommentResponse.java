package com.ijaa.event_service.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCommentResponse {

    private Long id;
    private Long eventId;
    private String username;
    private String authorName; // Full name of the commenter
    private String content;
    private Boolean isEdited;
    private Boolean isDeleted;
    private Long parentCommentId;
    private Integer likes;
    private Integer replyCount;
    private Boolean isLikedByCurrentUser; // Whether current user has liked this comment
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<EventCommentResponse> replies; // For threaded comments
} 