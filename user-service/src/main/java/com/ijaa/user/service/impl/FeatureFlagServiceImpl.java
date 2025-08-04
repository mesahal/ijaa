package com.ijaa.user.service.impl;

import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureFlagServiceImpl implements FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;

    @Override
    public List<FeatureFlag> getAllFeatureFlags() {
        return featureFlagRepository.findAll();
    }

    @Override
    public FeatureFlag getFeatureFlag(String featureName) {
        return featureFlagRepository.findByFeatureName(featureName)
                .orElse(null);
    }

    @Override
    public FeatureFlag createFeatureFlag(String featureName, String description) {
        if (featureFlagRepository.existsByFeatureName(featureName)) {
            throw new RuntimeException("Feature flag with name " + featureName + " already exists");
        }

        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setFeatureName(featureName);
        featureFlag.setDescription(description);
        featureFlag.setEnabled(false); // Default to disabled

        return featureFlagRepository.save(featureFlag);
    }

    @Override
    public FeatureFlag updateFeatureFlag(String featureName, boolean enabled) {
        FeatureFlag featureFlag = featureFlagRepository.findByFeatureName(featureName)
                .orElseThrow(() -> new RuntimeException("Feature flag not found: " + featureName));

        featureFlag.setEnabled(enabled);
        return featureFlagRepository.save(featureFlag);
    }

    @Override
    public void deleteFeatureFlag(String featureName) {
        FeatureFlag featureFlag = featureFlagRepository.findByFeatureName(featureName)
                .orElseThrow(() -> new RuntimeException("Feature flag not found: " + featureName));

        featureFlagRepository.delete(featureFlag);
    }

    @Override
    public boolean isFeatureEnabled(String featureName) {
        FeatureFlag featureFlag = featureFlagRepository.findByFeatureName(featureName)
                .orElse(null);

        return featureFlag != null && featureFlag.getEnabled();
    }
} 