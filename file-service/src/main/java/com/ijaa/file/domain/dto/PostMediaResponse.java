package com.ijaa.file.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMediaResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private String mediaType; // IMAGE, VIDEO
    private Long fileSize;
    private Integer fileOrder;
    private LocalDateTime createdAt;
}
