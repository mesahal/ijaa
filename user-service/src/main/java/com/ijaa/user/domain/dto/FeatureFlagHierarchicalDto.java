package com.ijaa.user.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagHierarchicalDto {
    private Long id;
    private String name;
    private String displayName;
    private Long parentId;
    private Boolean enabled;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FeatureFlagHierarchicalDto> children;
}
