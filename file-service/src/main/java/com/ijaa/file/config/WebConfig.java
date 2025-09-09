package com.ijaa.file.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.storage.base-path}")
    private String basePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ensure the path ends with a separator
        String normalizedPath = basePath.endsWith(File.separator) ? basePath : basePath + File.separator;

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + normalizedPath)
                .setCachePeriod(3600); // Cache for 1 hour

        log.info("Configured resource handler: /uploads/** -> file:{}", normalizedPath);
    }

}
