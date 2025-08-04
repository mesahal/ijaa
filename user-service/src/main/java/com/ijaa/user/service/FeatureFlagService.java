package com.ijaa.user.service;

import com.ijaa.user.domain.entity.FeatureFlag;

import java.util.List;

public interface FeatureFlagService {

    List<FeatureFlag> getAllFeatureFlags();
    
    FeatureFlag getFeatureFlag(String featureName);
    
    FeatureFlag createFeatureFlag(String featureName, String description);
    
    FeatureFlag updateFeatureFlag(String featureName, boolean enabled);
    
    void deleteFeatureFlag(String featureName);
    
    boolean isFeatureEnabled(String featureName);
} 