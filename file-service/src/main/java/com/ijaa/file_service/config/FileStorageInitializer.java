package com.ijaa.file_service.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileStorageInitializer {

    @Value("${file.storage.base-path}")
    private String basePath;

    @Value("${file.storage.profile-photos-path}")
    private String profilePhotosPath;

    @Value("${file.storage.cover-photos-path}")
    private String coverPhotosPath;

    @PostConstruct
    public void initializeDirectories() {
        try {
            // Create base directory
            Path baseDir = Paths.get(basePath);
            Files.createDirectories(baseDir);
            log.info("Base directory created/verified: {}", baseDir.toAbsolutePath());

            // Create profile photos directory
            Path profileDir = Paths.get(profilePhotosPath);
            Files.createDirectories(profileDir);
            log.info("Profile photos directory created/verified: {}", profileDir.toAbsolutePath());

            // Create cover photos directory
            Path coverDir = Paths.get(coverPhotosPath);
            Files.createDirectories(coverDir);
            log.info("Cover photos directory created/verified: {}", coverDir.toAbsolutePath());

            // Check write permissions
            if (!Files.isWritable(baseDir)) {
                throw new IllegalStateException("No write permission for base directory: " + baseDir);
            }

        } catch (IOException e) {
            log.error("Failed to initialize file storage directories", e);
            throw new RuntimeException("Could not initialize file storage directories", e);
        }
    }
}
