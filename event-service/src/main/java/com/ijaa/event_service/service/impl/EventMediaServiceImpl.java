package com.ijaa.event_service.service.impl;

import com.ijaa.event_service.domain.common.PagedResponse;
import com.ijaa.event_service.domain.entity.EventMedia;
import com.ijaa.event_service.domain.request.EventMediaRequest;
import com.ijaa.event_service.domain.response.EventMediaResponse;
import com.ijaa.event_service.repository.EventMediaRepository;
import com.ijaa.event_service.repository.EventRepository;
import com.ijaa.event_service.service.EventMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventMediaServiceImpl implements EventMediaService {

    private final EventMediaRepository eventMediaRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventMediaResponse uploadMedia(EventMediaRequest request, String username) {
        log.info("Uploading media for event: {} by user: {}", request.getEventId(), username);

        // Verify event exists
        if (!eventRepository.existsById(request.getEventId())) {
            throw new RuntimeException("Event not found");
        }

        EventMedia media = new EventMedia();
        media.setEventId(request.getEventId());
        media.setUploadedByUsername(username);
        media.setFileName(request.getFileName());
        media.setFileUrl(request.getFileUrl());
        media.setFileType(request.getFileType());
        media.setFileSize(request.getFileSize());
        media.setCaption(request.getCaption());
        media.setMediaType(EventMedia.MediaType.valueOf(request.getMediaType()));

        EventMedia savedMedia = eventMediaRepository.save(media);
        return mapToResponse(savedMedia);
    }

    @Override
    public List<EventMediaResponse> getEventMedia(Long eventId) {
        log.info("Getting media for event: {}", eventId);

        List<EventMedia> mediaList = eventMediaRepository.findByEventIdAndIsApprovedTrueOrderByCreatedAtDesc(eventId);
        return mediaList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventMediaResponse> getEventMediaByType(Long eventId, String mediaType) {
        log.info("Getting media for event: {} by type: {}", eventId, mediaType);

        EventMedia.MediaType type = EventMedia.MediaType.valueOf(mediaType.toUpperCase());
        List<EventMedia> mediaList = eventMediaRepository.findByEventIdAndMediaTypeAndIsApprovedTrueOrderByCreatedAtDesc(eventId, type);
        return mediaList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EventMediaResponse getMedia(Long mediaId) {
        log.info("Getting media: {}", mediaId);

        EventMedia media = eventMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        return mapToResponse(media);
    }

    @Override
    @Transactional
    public EventMediaResponse updateMediaCaption(Long mediaId, String caption, String username) {
        log.info("Updating media caption: {} by user: {}", mediaId, username);

        EventMedia media = eventMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        if (!media.getUploadedByUsername().equals(username)) {
            throw new RuntimeException("Not authorized to update this media");
        }

        media.setCaption(caption);
        EventMedia updatedMedia = eventMediaRepository.save(media);
        return mapToResponse(updatedMedia);
    }

    @Override
    @Transactional
    public void deleteMedia(Long mediaId, String username) {
        log.info("Deleting media: {} by user: {}", mediaId, username);

        EventMedia media = eventMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        if (!media.getUploadedByUsername().equals(username)) {
            throw new RuntimeException("Not authorized to delete this media");
        }

        eventMediaRepository.delete(media);
    }

    @Override
    @Transactional
    public EventMediaResponse toggleLike(Long mediaId, String username) {
        log.info("Toggling like for media: {} by user: {}", mediaId, username);

        EventMedia media = eventMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        // Simple like toggle (in a real app, you'd have a separate likes table)
        media.setLikes(media.getLikes() + 1);

        EventMedia updatedMedia = eventMediaRepository.save(media);
        return mapToResponse(updatedMedia);
    }

    @Override
    public PagedResponse<EventMediaResponse> getUserMedia(String username, int page, int size) {
        log.info("Getting media by user: {}, page: {}, size: {}", username, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventMedia> mediaPage = eventMediaRepository
                .findByUploadedByUsernameAndIsApprovedTrueOrderByCreatedAtDesc(username, pageable);

        List<EventMediaResponse> responses = mediaPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<EventMediaResponse>(
                responses,
                mediaPage.getNumber(),
                mediaPage.getSize(),
                mediaPage.getTotalElements(),
                mediaPage.getTotalPages(),
                mediaPage.isFirst(),
                mediaPage.isLast()
        );
    }

    @Override
    public PagedResponse<EventMediaResponse> getRecentMedia(int page, int size) {
        log.info("Getting recent media, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventMedia> mediaPage = eventMediaRepository.findRecentMedia(pageable);

        List<EventMediaResponse> responses = mediaPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<EventMediaResponse>(
                responses,
                mediaPage.getNumber(),
                mediaPage.getSize(),
                mediaPage.getTotalElements(),
                mediaPage.getTotalPages(),
                mediaPage.isFirst(),
                mediaPage.isLast()
        );
    }

    @Override
    public PagedResponse<EventMediaResponse> getPopularMedia(int page, int size) {
        log.info("Getting popular media, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventMedia> mediaPage = eventMediaRepository.findPopularMedia(pageable);

        List<EventMediaResponse> responses = mediaPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<EventMediaResponse>(
                responses,
                mediaPage.getNumber(),
                mediaPage.getSize(),
                mediaPage.getTotalElements(),
                mediaPage.getTotalPages(),
                mediaPage.isFirst(),
                mediaPage.isLast()
        );
    }

    @Override
    @Transactional
    public EventMediaResponse approveMedia(Long mediaId, boolean approved) {
        log.info("Approving media: {} with status: {}", mediaId, approved);

        EventMedia media = eventMediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media not found"));

        media.setIsApproved(approved);
        EventMedia updatedMedia = eventMediaRepository.save(media);
        return mapToResponse(updatedMedia);
    }

    @Override
    public PagedResponse<EventMediaResponse> getPendingApprovalMedia(int page, int size) {
        log.info("Getting pending approval media, page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<EventMedia> mediaPage = eventMediaRepository.findPendingApproval(pageable);

        List<EventMediaResponse> responses = mediaPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<EventMediaResponse>(
                responses,
                mediaPage.getNumber(),
                mediaPage.getSize(),
                mediaPage.getTotalElements(),
                mediaPage.getTotalPages(),
                mediaPage.isFirst(),
                mediaPage.isLast()
        );
    }

    private EventMediaResponse mapToResponse(EventMedia media) {
        return new EventMediaResponse(
                media.getId(),
                media.getEventId(),
                media.getUploadedByUsername(),
                media.getFileName(),
                media.getFileUrl(),
                media.getFileType(),
                media.getFileSize(),
                media.getCaption(),
                media.getMediaType().name(),
                media.getIsApproved(),
                media.getLikes(),
                media.getCreatedAt(),
                media.getUpdatedAt()
        );
    }
} 