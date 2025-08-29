package com.ijaa.event_service.common.exceptions;

/**
 * Exception thrown when a feature flag is disabled but the feature is being accessed.
 */
public class FeatureDisabledException extends RuntimeException {
    
    private final String featureName;
    
    public FeatureDisabledException(String featureName) {
        super("Feature '" + featureName + "' is disabled");
        this.featureName = featureName;
    }
    
    public FeatureDisabledException(String featureName, String message) {
        super(message != null && !message.isEmpty() ? message : "Feature '" + featureName + "' is disabled");
        this.featureName = featureName;
    }
    
    public String getFeatureName() {
        return featureName;
    }
}


