package com.ijaa.event.presenter.rest.client;

import com.ijaa.event.config.FeignConfig;
import com.ijaa.event.domain.response.PostMediaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "file-service", configuration = FeignConfig.class)
public interface FileServiceClient {

    @PostMapping(value = "/api/v1/events/{eventId}/banner", consumes = "multipart/form-data")
    Object uploadEventBanner(@PathVariable String eventId, @RequestPart("file") MultipartFile file, @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/events/{eventId}/banner")
    Object getEventBannerUrl(@PathVariable String eventId, @RequestHeader("Authorization") String token);

    @DeleteMapping("/api/v1/events/{eventId}/banner")
    Object deleteEventBanner(@PathVariable String eventId, @RequestHeader("Authorization") String token);

    // Post media methods
    @GetMapping("/api/v1/files/posts/{postId}/media")
    List<PostMediaResponse> getPostMedia(@PathVariable String postId);

    @PostMapping(value = "/api/v1/files/posts/{postId}/media", consumes = "multipart/form-data")
    Object uploadPostMedia(@PathVariable String postId, @RequestPart("file") MultipartFile file, 
                          @RequestPart("mediaType") String mediaType, @RequestHeader("Authorization") String token,
                          @RequestHeader("X-Username") String username);

    @DeleteMapping("/api/v1/files/posts/{postId}/media/{fileName}")
    Object deletePostMedia(@PathVariable String postId, @PathVariable String fileName, 
                          @RequestHeader("Authorization") String token, @RequestHeader("X-Username") String username);
}
