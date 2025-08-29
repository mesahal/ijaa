package com.ijaa.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.dto.AlumniSearchMetadata;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.repository.ConnectionRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.service.AlumniSearchService;
import com.ijaa.user.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlumniSearchServiceImpl extends BaseService implements AlumniSearchService {

    private final ConnectionRepository connectionRepository;
    private final ProfileRepository profileRepository;
    private final InterestRepository interestRepository;

    public AlumniSearchServiceImpl(
            ConnectionRepository connectionRepository,
            ProfileRepository profileRepository,
            InterestRepository interestRepository,
            ObjectMapper objectMapper) {

        super(objectMapper);
        this.connectionRepository = connectionRepository;
        this.profileRepository = profileRepository;
        this.interestRepository = interestRepository;
    }

    @Override
    public PagedResponse<AlumniSearchDto> searchAlumni(AlumniSearchRequest request) {
        String currentUsername = getCurrentUsername();

        // Validate and sanitize request parameters
        validateAndSanitizeRequest(request);

        // Get connected usernames for current user
        List<String> connectedUsernames = connectionRepository.findConnectedUsernames(currentUsername);

        // Create pageable with sorting
        Pageable pageable = createPageable(request);

        // Search alumni directly from profiles table with filters
        Page<Profile> profilePage = profileRepository.findProfilesWithFilters(
                request.getSearchQuery(),
                request.getBatch(),
                request.getProfession(),
                request.getLocation(),
                currentUsername, // Exclude current user
                pageable
        );

        // Get all userIds from the profile results
        List<String> userIds = profilePage.getContent().stream()
                .map(Profile::getUserId)
                .collect(Collectors.toList());

        // Fetch all interests for these users in one query
        Map<String, List<String>> userInterestsMap = interestRepository
                .findByUserIdIn(userIds)
                .stream()
                .collect(Collectors.groupingBy(
                        interest -> interest.getUserId(),
                        Collectors.mapping(interest -> interest.getInterest(), Collectors.toList())
                ));

        // Convert to DTOs
        List<AlumniSearchDto> alumniDtos = profilePage.getContent().stream()
                .map(profile -> toDto(
                        profile,
                        connectedUsernames.contains(profile.getUsername()),
                        userInterestsMap.getOrDefault(profile.getUserId(), List.of())
                ))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                alumniDtos,
                profilePage.getNumber(),
                profilePage.getSize(),
                profilePage.getTotalElements(),
                profilePage.getTotalPages(),
                profilePage.isFirst(),
                profilePage.isLast()
        );
    }

    @Override
    public AlumniSearchMetadata getSearchMetadata() {
        String currentUsername = getCurrentUsername();
        
        // Get total count of alumni (excluding current user)
        long totalAlumni = profileRepository.countByUsernameNot(currentUsername);
        
        // Get available batches
        List<String> availableBatches = profileRepository.findDistinctBatchesByUsernameNot(currentUsername);
        
        // Get available professions
        List<String> availableProfessions = profileRepository.findDistinctProfessionsByUsernameNot(currentUsername);
        
        // Get available locations
        List<String> availableLocations = profileRepository.findDistinctLocationsByUsernameNot(currentUsername);
        
        return new AlumniSearchMetadata(
                totalAlumni,
                availableBatches,
                availableProfessions,
                availableLocations,
                12, // defaultPageSize
                100, // maxPageSize
                1000, // maxPageNumber
                List.of("relevance", "name", "batch", "connections") // availableSortOptions
        );
    }

    private void validateAndSanitizeRequest(AlumniSearchRequest request) {
        // Sanitize search query to prevent SQL injection
        if (request.getSearchQuery() != null) {
            request.setSearchQuery(request.getSearchQuery().trim());
            if (request.getSearchQuery().length() > 100) {
                request.setSearchQuery(request.getSearchQuery().substring(0, 100));
            }
        }

        // Sanitize other string fields
        if (request.getBatch() != null) {
            request.setBatch(request.getBatch().trim());
        }
        if (request.getProfession() != null) {
            request.setProfession(request.getProfession().trim());
            if (request.getProfession().length() > 50) {
                request.setProfession(request.getProfession().substring(0, 50));
            }
        }
        if (request.getLocation() != null) {
            request.setLocation(request.getLocation().trim());
            if (request.getLocation().length() > 100) {
                request.setLocation(request.getLocation().substring(0, 100));
            }
        }

        // Ensure sortBy is valid
        if (request.getSortBy() == null || request.getSortBy().trim().isEmpty()) {
            request.setSortBy("relevance");
        }
    }

    private Pageable createPageable(AlumniSearchRequest request) {
        Sort sort = Sort.by("createdAt").descending(); // default sort

        switch (request.getSortBy().toLowerCase()) {
            case "name":
                sort = Sort.by("name").ascending();
                break;
            case "batch":
                sort = Sort.by("batch").descending();
                break;
            case "connections":
                sort = Sort.by("connections").descending();
                break;
            case "relevance":
            default:
                sort = Sort.by("createdAt").descending();
                break;
        }

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private AlumniSearchDto toDto(Profile profile, boolean isConnected, List<String> interests) {
        AlumniSearchDto dto = new AlumniSearchDto();
        dto.setUserId(profile.getUserId());
        dto.setName(profile.getName());
        dto.setBatch(profile.getBatch());
        dto.setProfession(profile.getProfession());
        dto.setLocation(profile.getLocation());
        dto.setBio(profile.getBio());
        dto.setConnections(profile.getConnections());
        dto.setIsConnected(isConnected);
        dto.setInterests(interests); // Now populated from Interest entity

        // Set default avatar if not available
        dto.setAvatar("https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=1");

        return dto;
    }
}
