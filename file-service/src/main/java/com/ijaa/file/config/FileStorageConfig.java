package com.ijaa.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "file.storage")
public class FileStorageConfig {

    private String basePath;
    private String profilePhotosPath;
    private String coverPhotosPath;
    private String eventBannersPath;
    private List<String> allowedImageTypes;
    private int maxFileSizeMb;

    // Getters for backward compatibility
    public String getBasePath() { return basePath; }
    public String getProfilePhotosPath() { return profilePhotosPath; }
    public String getCoverPhotosPath() { return coverPhotosPath; }
    public String getEventBannersPath() { return eventBannersPath; }
    public List<String> getAllowedImageTypes() { return allowedImageTypes; }
    public int getMaxFileSizeMb() { return maxFileSizeMb; }
}
