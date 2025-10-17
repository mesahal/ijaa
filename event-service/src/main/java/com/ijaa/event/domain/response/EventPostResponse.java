package com.ijaa.event.domain.response;

import com.ijaa.event.domain.entity.EventPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object for event post data")
public class EventPostResponse {

    @Schema(description = "Unique identifier of the post", example = "1")
    private Long id;
    
    @Schema(description = "ID of the event this post belongs to", example = "13")
    private Long eventId;
    
    @Schema(description = "Username of the post author", example = "mdsahal.info@gmail.com")
    private String username;
    
    @Schema(description = "User ID of the post author", example = "123")
    private String userId;
    
    @Schema(description = "Content of the post", example = "This is a great event!")
    private String content;
    
    @Schema(description = "Type of the post", example = "TEXT", allowableValues = {"TEXT", "IMAGE", "VIDEO", "MIXED"})
    private EventPost.PostType postType;
    
    @Schema(description = "Whether the post has been edited", example = "false")
    private Boolean isEdited;
    
    @Schema(description = "Whether the post has been deleted", example = "false")
    private Boolean isDeleted;
    
    @Schema(description = "Number of likes on the post", example = "5")
    private Integer likes;
    
    @Schema(description = "Number of comments on the post", example = "3")
    private Integer commentsCount;
    
    @Schema(description = "When the post was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "When the post was last updated", example = "2024-01-15T10:30:00")
    private LocalDateTime updatedAt;
    
    @Schema(description = "Media files associated with this post")
    private List<PostMediaResponse> mediaFiles;
    
    @Schema(description = "Whether the current user has liked this post", example = "false")
    private Boolean isLikedByUser;
    
    @Schema(description = "Recent comments on this post (for preview)")
    private List<EventCommentResponse> recentComments;
    
    @Schema(description = "Name of the event creator", example = "John Doe")
    private String creatorName;

}
