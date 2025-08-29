package com.ijaa.event_service.common.aspect;

import com.ijaa.event_service.common.annotation.RequiresFeature;
import com.ijaa.event_service.common.exceptions.FeatureDisabledException;
import com.ijaa.event_service.common.utils.FeatureFlagUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class FeatureFlagAspect {

    private final FeatureFlagUtils featureFlagUtils;

    @Before("@annotation(requiresFeature)")
    public void checkFeatureFlag(JoinPoint joinPoint, RequiresFeature requiresFeature) {
        String featureName = requiresFeature.value();
        String customMessage = requiresFeature.message();
        
        log.debug("Checking feature flag: {} for method: {}", featureName, joinPoint.getSignature().getName());
        
        if (!featureFlagUtils.isFeatureEnabled(featureName)) {
            String message = customMessage != null && !customMessage.isEmpty() 
                ? customMessage 
                : "Feature '" + featureName + "' is disabled";
            
            log.warn("Feature flag '{}' is disabled. Blocking access to method: {}", 
                    featureName, joinPoint.getSignature().getName());
            
            throw new FeatureDisabledException(featureName, message);
        }
        
        log.debug("Feature flag: {} is enabled. Allowing access to method: {}", 
                featureName, joinPoint.getSignature().getName());
    }
}


