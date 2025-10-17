package com.ijaa.event.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response object for post media file data")
public class PostMediaResponse {
    @Schema(description = "Unique identifier of the media file", example = "1")
    private Long id;
    
    @Schema(description = "Original name of the file", example = "image.jpg")
    private String fileName;
    
    @Schema(description = "URL to access the file", example = "https://example.com/files/image.jpg")
    private String fileUrl;
    
    @Schema(description = "MIME type of the file", example = "image/jpeg")
    private String fileType;
    
    @Schema(description = "Media type category", example = "IMAGE", allowableValues = {"IMAGE", "VIDEO"})
    private String mediaType;
    
    @Schema(description = "Size of the file in bytes", example = "1024000")
    private Long fileSize;
    
    @Schema(description = "Order of the file in the post", example = "1")
    private Integer fileOrder;
    
    @Schema(description = "When the file was uploaded", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;
}
