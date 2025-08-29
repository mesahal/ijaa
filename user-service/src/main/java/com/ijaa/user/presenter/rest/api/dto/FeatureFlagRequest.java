package com.ijaa.user.presenter.rest.api.dto;

import lombok.Data;

@Data
public class FeatureFlagRequest {
    private String name;
    private String displayName;
    private String description;
    private Long parentId;
    private Boolean enabled;
}

