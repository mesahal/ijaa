package com.ijaa.event.service;

import com.ijaa.event.domain.entity.Event;
import com.ijaa.event.domain.entity.EventPost;
import com.ijaa.event.domain.request.EventPostRequest;
import com.ijaa.event.domain.response.PostMediaResponse;
import com.ijaa.event.presenter.rest.client.FileServiceClient;
import com.ijaa.event.repository.EventPostRepository;
import com.ijaa.event.repository.EventRepository;
import com.ijaa.event.service.impl.EventPostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventPostServiceTest {

    @Mock
    private EventPostRepository eventPostRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private FileServiceClient fileServiceClient;

    @InjectMocks
    private EventPostServiceImpl eventPostService;

    private Event testEvent;
    private EventPost testPost;
    private EventPostRequest testRequest;

    @BeforeEach
    void setUp() {
        // Create test event
        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setTitle("Test Event");
        testEvent.setActive(true);
        testEvent.setCreatedByUsername("testuser");

        // Create test post
        testPost = new EventPost();
        testPost.setId(1L);
        testPost.setEventId(1L);
        testPost.setUsername("testuser");
        testPost.setContent("Test post content");
        testPost.setPostType(EventPost.PostType.MIXED);
        testPost.setCreatedAt(LocalDateTime.now());

        // Create test request
        testRequest = new EventPostRequest();
        testRequest.setEventId(1L);
        testRequest.setContent("Test post content");
        testRequest.setPostType(EventPost.PostType.MIXED);
    }

    @Test
    void testCreatePost() {
        // Given
        when(eventRepository.findById(1L)).thenReturn(Optional.of(testEvent));
        when(eventPostRepository.save(any(EventPost.class))).thenReturn(testPost);

        // When
        var result = eventPostService.createPostWithContentAndMedia(1L, "Test post content", null, "testuser");

        // Then
        assertNotNull(result);
        assertEquals("Test post content", result.getContent());
        assertEquals(EventPost.PostType.MIXED, result.getPostType());
        verify(eventPostRepository).save(any(EventPost.class));
    }

    @Test
    void testMapToResponseWithMediaFiles() {
        // Given
        List<PostMediaResponse> mockMediaFiles = Arrays.asList(
                PostMediaResponse.builder()
                        .id(1L)
                        .fileName("test-image.jpg")
                        .fileUrl("/ijaa/api/v1/files/posts/1/media/test-image.jpg")
                        .fileType("image/jpeg")
                        .mediaType("IMAGE")
                        .fileSize(1024L)
                        .fileOrder(0)
                        .createdAt(LocalDateTime.now())
                        .build(),
                PostMediaResponse.builder()
                        .id(2L)
                        .fileName("test-video.mp4")
                        .fileUrl("/ijaa/api/v1/files/posts/1/media/test-video.mp4")
                        .fileType("video/mp4")
                        .mediaType("VIDEO")
                        .fileSize(2048L)
                        .fileOrder(1)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        when(fileServiceClient.getPostMedia("1")).thenReturn(mockMediaFiles);
        when(eventPostRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        var result = eventPostService.getPost(1L, "testuser");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getMediaFiles().size());
        assertEquals("test-image.jpg", result.getMediaFiles().get(0).getFileName());
        assertEquals("test-video.mp4", result.getMediaFiles().get(1).getFileName());
        verify(fileServiceClient).getPostMedia("1");
    }

    @Test
    void testMapToResponseWithNoMediaFiles() {
        // Given
        when(fileServiceClient.getPostMedia("1")).thenReturn(Arrays.asList());
        when(eventPostRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        var result = eventPostService.getPost(1L, "testuser");

        // Then
        assertNotNull(result);
        assertTrue(result.getMediaFiles().isEmpty());
        verify(fileServiceClient).getPostMedia("1");
    }

    @Test
    void testMapToResponseWithFileServiceError() {
        // Given
        when(fileServiceClient.getPostMedia("1")).thenThrow(new RuntimeException("File service error"));
        when(eventPostRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // When
        var result = eventPostService.getPost(1L, "testuser");

        // Then
        assertNotNull(result);
        assertTrue(result.getMediaFiles().isEmpty()); // Should return empty list on error
        verify(fileServiceClient).getPostMedia("1");
    }
}
