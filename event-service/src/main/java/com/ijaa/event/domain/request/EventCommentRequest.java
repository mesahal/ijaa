package com.ijaa.event.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventCommentRequest {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String content;

    @NotBlank(message = "Author name is required")
    @Size(min = 1, max = 100, message = "Author name must be between 1 and 100 characters")
    private String authorName; // Full name of the commenter

    @NotBlank(message = "User ID is required")
    @Size(min = 1, max = 50, message = "User ID must be between 1 and 50 characters")
    private String userId; // User ID for profile photo

    private Long parentCommentId; // For replies, null for top-level comments
} 
