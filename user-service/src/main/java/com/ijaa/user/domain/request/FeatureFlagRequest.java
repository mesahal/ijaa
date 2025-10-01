package com.ijaa.user.domain.request;

import lombok.Data;

@Data
public class FeatureFlagRequest {
    private String name;
    private String displayName;
    private String description;
    private Long parentId;
    private Boolean enabled;
}

