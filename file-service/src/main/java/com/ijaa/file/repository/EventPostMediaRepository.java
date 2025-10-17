package com.ijaa.file.repository;

import com.ijaa.file.domain.entity.EventPostMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventPostMediaRepository extends JpaRepository<EventPostMedia, Long> {

    // Find all media files for a post
    List<EventPostMedia> findByPostIdOrderByFileOrderAsc(String postId);

    // Find media files by post ID and media type
    List<EventPostMedia> findByPostIdAndMediaTypeOrderByFileOrderAsc(String postId, EventPostMedia.MediaType mediaType);

    // Find a specific media file by post ID and file name
    Optional<EventPostMedia> findByPostIdAndFileName(String postId, String fileName);

    // Count media files for a post
    Long countByPostId(String postId);

    // Count media files by post ID and media type
    Long countByPostIdAndMediaType(String postId, EventPostMedia.MediaType mediaType);

    // Delete all media files for a post
    void deleteByPostId(String postId);

    // Delete a specific media file
    void deleteByPostIdAndFileName(String postId, String fileName);
}
