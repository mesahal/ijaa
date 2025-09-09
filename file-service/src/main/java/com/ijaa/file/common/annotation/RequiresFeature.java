package com.ijaa.file.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that require a specific feature flag to be enabled.
 * If the feature flag is disabled, the method will throw a FeatureDisabledException.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresFeature {
    /**
     * The name of the feature flag that must be enabled
     */
    String value();
    
    /**
     * Optional message to include in the exception when the feature is disabled
     */
    String message() default "";
}

