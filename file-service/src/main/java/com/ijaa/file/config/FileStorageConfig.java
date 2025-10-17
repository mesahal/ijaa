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
    private String eventPostMediaPath;
    private List<String> allowedImageTypes;
    private List<String> allowedVideoTypes;
    private int maxFileSizeMb;
    private int maxVideoSizeMb;

    // Getters for backward compatibility
    public String getBasePath() { return basePath; }
    public String getProfilePhotosPath() { return profilePhotosPath; }
    public String getCoverPhotosPath() { return coverPhotosPath; }
    public String getEventBannersPath() { return eventBannersPath; }
    public String getEventPostMediaPath() { return eventPostMediaPath; }
    public List<String> getAllowedImageTypes() { return allowedImageTypes; }
    public List<String> getAllowedVideoTypes() { return allowedVideoTypes; }
    public int getMaxFileSizeMb() { return maxFileSizeMb; }
    public int getMaxVideoSizeMb() { return maxVideoSizeMb; }
}
