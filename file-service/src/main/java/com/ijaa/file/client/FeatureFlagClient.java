package com.ijaa.file.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the user service to check feature flags
 */
@FeignClient(name = "user-service", fallback = FeatureFlagClientFallback.class)
public interface FeatureFlagClient {

    /**
     * Check if a feature flag is enabled
     * @param featureName the name of the feature flag
     * @return ApiResponse containing the feature flag status
     */
    @GetMapping("/api/v1/admin/feature-flags/{featureName}/enabled")
    ApiResponse<FeatureFlagStatus> checkFeatureFlag(@PathVariable("featureName") String featureName);

    /**
     * Response wrapper class for API responses
     */
    class ApiResponse<T> {
        private String message;
        private String code;
        private T data;

        public ApiResponse() {}

        public ApiResponse(String message, String code, T data) {
            this.message = message;
            this.code = code;
            this.data = data;
        }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public T getData() { return data; }
        public void setData(T data) { this.data = data; }
    }

    /**
     * Feature flag status response class
     */
    class FeatureFlagStatus {
        private String name;
        private boolean enabled;

        public FeatureFlagStatus() {}

        public FeatureFlagStatus(String name, boolean enabled) {
            this.name = name;
            this.enabled = enabled;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}
