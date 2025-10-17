package com.ijaa.file.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import java.io.IOException;

import java.io.File;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.storage.base-path}")
    private String basePath;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Ensure the path ends with a separator
        String normalizedPath = basePath.endsWith(File.separator) ? basePath : basePath + File.separator;

        // Only handle specific static resource paths, not API paths
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + normalizedPath)
                .setCachePeriod(3600) // Cache for 1 hour
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        // Handle static files for serving uploaded files (not API endpoints)
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + normalizedPath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        log.info("Configured resource handler: /uploads/** -> file:{}", normalizedPath);
        log.info("Configured file serving handler: /files/** -> file:{}", normalizedPath);
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
                log.info("Request received: {} {} - Handler: {}", request.getMethod(), request.getRequestURI(), handler.getClass().getSimpleName());
                return true;
            }
        });
    }


    // Custom resource resolver that rejects API paths
    public static class ApiPathRejectingResourceResolver extends PathResourceResolver {
        @Override
        protected Resource getResource(@NonNull String resourcePath, @NonNull Resource location) throws IOException {
            // Reject any API paths
            if (resourcePath.startsWith("api/") || resourcePath.startsWith("ijaa/")) {
                log.debug("Rejecting API path as static resource: {}", resourcePath);
                return null;
            }
            return super.getResource(resourcePath, location);
        }
    }

}
