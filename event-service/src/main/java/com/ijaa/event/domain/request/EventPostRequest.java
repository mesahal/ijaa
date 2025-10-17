package com.ijaa.event.domain.request;

import com.ijaa.event.domain.entity.EventPost;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new event post")
public class EventPostRequest {

    @NotNull(message = "Event ID is required")
    @Schema(description = "ID of the event this post belongs to", example = "13", required = true)
    private Long eventId;

    @Size(max = 2000, message = "Content must not exceed 2000 characters")
    @Schema(description = "Content of the post", example = "This is a great event!", maxLength = 2000)
    private String content;

    @NotNull(message = "Post type is required")
    @Schema(description = "Type of the post", example = "TEXT", allowableValues = {"TEXT", "IMAGE", "VIDEO", "MIXED"}, required = true)
    private EventPost.PostType postType;

    // For media posts, this will be handled separately via file upload
    // The post will be created first, then media files will be attached
}
