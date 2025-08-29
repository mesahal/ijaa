package com.ijaa.user.service;

import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.dto.FeatureFlagDto;

import java.util.List;

public interface FeatureFlagService {

    List<FeatureFlagDto> getAllFlags();
    
    FeatureFlag getFeatureFlag(String flagName);
    
    FeatureFlag createFlag(FeatureFlagDto dto);
    
    FeatureFlag updateFlag(String flagName, boolean enabled);
    
    void deleteFeatureFlag(String flagName);
    
    boolean isEnabled(String flagName);
} 