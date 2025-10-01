package com.ijaa.user.service.impl;

import com.ijaa.user.domain.entity.FeatureFlag;
import com.ijaa.user.domain.dto.FeatureFlagDto;
import com.ijaa.user.domain.mapper.FeatureFlagMapper;
import com.ijaa.user.repository.FeatureFlagRepository;
import com.ijaa.user.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagServiceImpl implements FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final FeatureFlagMapper featureFlagMapper;
    
    // In-memory cache for fast lookups
    private final Map<String, Boolean> flagCache = new ConcurrentHashMap<>();

    @Override
    @Cacheable("featureFlags")
    public List<FeatureFlagDto> getAllFlags() {
        List<FeatureFlag> topLevelFlags = featureFlagRepository.findAllTopLevelFlags();
        return topLevelFlags.stream()
                .map(featureFlagMapper::toDtoWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    public FeatureFlag getFeatureFlag(String flagName) {
        return featureFlagRepository.findByName(flagName)
                .orElse(null);
    }

    @Override
    @CacheEvict(value = "featureFlags", allEntries = true)
    public FeatureFlag createFlag(FeatureFlagDto dto) {
        if (featureFlagRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Feature flag with name " + dto.getName() + " already exists");
        }

        FeatureFlag featureFlag = new FeatureFlag();
        featureFlag.setName(dto.getName());
        featureFlag.setDisplayName(dto.getDisplayName());
        featureFlag.setDescription(dto.getDescription());
        featureFlag.setEnabled(dto.getEnabled() != null ? dto.getEnabled() : false);

        // Set parent if provided
        if (dto.getParentId() != null) {
            FeatureFlag parent = featureFlagRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent feature flag not found: " + dto.getParentId()));
            featureFlag.setParent(parent);
        }

        FeatureFlag savedFlag = featureFlagRepository.save(featureFlag);
        
        // Update cache
        flagCache.put(savedFlag.getName(), savedFlag.getEnabled());
        
        return savedFlag;
    }

    @Override
    @CacheEvict(value = "featureFlags", allEntries = true)
    public FeatureFlag updateFlag(String flagName, boolean enabled) {
        FeatureFlag featureFlag = featureFlagRepository.findByName(flagName)
                .orElseThrow(() -> new RuntimeException("Feature flag not found: " + flagName));

        featureFlag.setEnabled(enabled);
        FeatureFlag savedFlag = featureFlagRepository.save(featureFlag);
        
        // Update cache
        flagCache.put(savedFlag.getName(), savedFlag.getEnabled());
        
        // Clear cache for child flags if this is a parent flag
        if (featureFlag.getChildren() != null && !featureFlag.getChildren().isEmpty()) {
            clearChildFlagsCache(featureFlag);
        }
        
        return savedFlag;
    }

    @Override
    @CacheEvict(value = "featureFlags", allEntries = true)
    public void deleteFeatureFlag(String flagName) {
        FeatureFlag featureFlag = featureFlagRepository.findByName(flagName)
                .orElseThrow(() -> new RuntimeException("Feature flag not found: " + flagName));

        featureFlagRepository.delete(featureFlag);
        
        // Remove from cache
        flagCache.remove(flagName);
    }

    @Override
    public boolean isEnabled(String flagName) {
        // Check cache first
        Boolean cachedValue = flagCache.get(flagName);
        if (cachedValue != null) {
            return cachedValue;
        }

        FeatureFlag featureFlag = featureFlagRepository.findByName(flagName)
                .orElse(null);

        if (featureFlag == null) {
            return false;
        }

        // Check if parent is disabled (hierarchical behavior)
        if (featureFlag.getParent() != null && !isEnabled(featureFlag.getParent().getName())) {
            flagCache.put(flagName, false);
            return false;
        }

        boolean enabled = featureFlag.getEnabled();
        flagCache.put(flagName, enabled);
        return enabled;
    }



    // Helper method to check if a flag itself is enabled (not considering children)
    private boolean isFlagEnabled(FeatureFlagDto flag) {
        return flag.getEnabled() != null && flag.getEnabled();
    }







    private void clearChildFlagsCache(FeatureFlag parentFlag) {
        if (parentFlag.getChildren() != null) {
            for (FeatureFlag child : parentFlag.getChildren()) {
                flagCache.remove(child.getName());
                clearChildFlagsCache(child);
            }
        }
    }
} 
