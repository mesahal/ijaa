package com.ijaa.user.domain.converter;

import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.dto.FeatureFlagDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FeatureFlagConverter {

    /**
     * Convert FeatureFlag entity to FeatureFlagDto
     */
    public FeatureFlagDto toDto(FeatureFlag featureFlag) {
        if (featureFlag == null) {
            return null;
        }

        FeatureFlagDto dto = new FeatureFlagDto();
        dto.setId(featureFlag.getId());
        dto.setName(featureFlag.getName());
        dto.setDisplayName(featureFlag.getDisplayName());
        dto.setDescription(featureFlag.getDescription());
        dto.setEnabled(featureFlag.getEnabled());
        dto.setCreatedAt(featureFlag.getCreatedAt());
        dto.setUpdatedAt(featureFlag.getUpdatedAt());
        
        if (featureFlag.getParent() != null) {
            dto.setParentId(featureFlag.getParent().getId());
        }
        
        return dto;
    }

    /**
     * Convert FeatureFlag entity to FeatureFlagDto with children (hierarchical structure)
     */
    public FeatureFlagDto toDtoWithChildren(FeatureFlag featureFlag) {
        return toDtoWithChildren(featureFlag, new java.util.HashSet<>());
    }

    /**
     * Convert FeatureFlag entity to FeatureFlagDto with children, preventing infinite recursion
     */
    private FeatureFlagDto toDtoWithChildren(FeatureFlag featureFlag, Set<Long> processedIds) {
        if (featureFlag == null) {
            return null;
        }

        // Prevent infinite recursion by checking if we've already processed this entity
        if (processedIds.contains(featureFlag.getId())) {
            FeatureFlagDto dto = new FeatureFlagDto();
            dto.setId(featureFlag.getId());
            dto.setName(featureFlag.getName());
            dto.setDisplayName(featureFlag.getDisplayName());
            dto.setDescription(featureFlag.getDescription());
            dto.setEnabled(featureFlag.getEnabled());
            dto.setCreatedAt(featureFlag.getCreatedAt());
            dto.setUpdatedAt(featureFlag.getUpdatedAt());
            
            if (featureFlag.getParent() != null) {
                dto.setParentId(featureFlag.getParent().getId());
            }
            
            // Don't set children to prevent infinite recursion
            return dto;
        }
        
        // Mark this entity as processed
        processedIds.add(featureFlag.getId());
        
        FeatureFlagDto dto = toDto(featureFlag);
        
        if (featureFlag.getChildren() != null && !featureFlag.getChildren().isEmpty()) {
            List<FeatureFlagDto> children = featureFlag.getChildren().stream()
                    .map(child -> toDtoWithChildren(child, processedIds))
                    .collect(Collectors.toList());
            dto.setChildren(children);
        } else {
            // Always set children to empty list if no children exist
            dto.setChildren(new ArrayList<>());
        }
        
        return dto;
    }

    /**
     * Convert list of FeatureFlag entities to list of FeatureFlagDto
     */
    public List<FeatureFlagDto> toDtoList(List<FeatureFlag> featureFlags) {
        if (featureFlags == null) {
            return new ArrayList<>();
        }
        
        return featureFlags.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert list of FeatureFlag entities to list of FeatureFlagDto with children
     */
    public List<FeatureFlagDto> toDtoListWithChildren(List<FeatureFlag> featureFlags) {
        if (featureFlags == null) {
            return new ArrayList<>();
        }
        
        return featureFlags.stream()
                .map(this::toDtoWithChildren)
                .collect(Collectors.toList());
    }
}

