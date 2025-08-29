package com.ijaa.user.presenter.rest.api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeatureFlagStatus {
    private String name;
    private boolean enabled;
}

