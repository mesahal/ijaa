package com.ijaa.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.common.PagedResponse;
import com.ijaa.user.domain.dto.AlumniSearchDto;
import com.ijaa.user.domain.request.AlumniSearchRequest;
import com.ijaa.user.domain.entity.AlumniSearch;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.repository.AlumniSearchRepository;
import com.ijaa.user.repository.ConnectionRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.service.AlumniSearchService;
import com.ijaa.user.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlumniSearchServiceImpl extends BaseService implements AlumniSearchService {

    public AlumniSearchServiceImpl(
            AlumniSearchRepository alumniSearchRepository,
            ConnectionRepository connectionRepository,
            ProfileRepository profileRepository,
            ObjectMapper objectMapper) {

        super(objectMapper); // Call parent constructor with ObjectMapper

        this.alumniSearchRepository = alumniSearchRepository;
        this.connectionRepository = connectionRepository;
        this.profileRepository = profileRepository;
    }

    private final AlumniSearchRepository alumniSearchRepository;
    private final ConnectionRepository connectionRepository;
    private final ProfileRepository profileRepository;

    @Override
    public PagedResponse<AlumniSearchDto> searchAlumni(AlumniSearchRequest request) {
        String currentUsername = getCurrentUsername();

        // Get connected usernames for current user
        List<String> connectedUsernames = connectionRepository.findConnectedUsernames(currentUsername);

        // Create pageable with sorting
        Pageable pageable = createPageable(request);

        // Search alumni with filters
        Page<AlumniSearch> alumniPage = alumniSearchRepository.findAlumniWithFilters(
                request.getSearchQuery(),
                request.getBatch(),
                request.getProfession(),
                request.getLocation(),
                pageable
        );

        // Convert to DTOs
        List<AlumniSearchDto> alumniDtos = alumniPage.getContent().stream()
                .map(alumni -> toDto(alumni, connectedUsernames.contains(alumni.getUsername())))
                .collect(Collectors.toList());

        return new PagedResponse<>(
                alumniDtos,
                alumniPage.getNumber(),
                alumniPage.getSize(),
                alumniPage.getTotalElements(),
                alumniPage.getTotalPages(),
                alumniPage.isFirst(),
                alumniPage.isLast()
        );
    }

    @Override
    public void syncAlumniProfile() {
        String username = getCurrentUsername();

        // Get profile information
        Profile profile = profileRepository.findByUsername(username).orElse(null);
        if (profile == null) return;

        // Get or create alumni search record
        AlumniSearch alumniSearch = alumniSearchRepository.findByUsername(username)
                .orElse(AlumniSearch.builder().username(username).build());

        // Sync data from profile
        alumniSearch.setName(profile.getName());
        alumniSearch.setBatch(profile.getBatch());
        alumniSearch.setProfession(profile.getProfession());
        alumniSearch.setLocation(profile.getLocation());
        alumniSearch.setBio(profile.getBio());

        // You can set avatar URL here based on your file storage logic
        if (alumniSearch.getAvatar() == null) {
            alumniSearch.setAvatar("https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg?auto=compress&cs=tinysrgb&w=150&h=150&dpr=1");
        }

        alumniSearchRepository.save(alumniSearch);
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

    private AlumniSearchDto toDto(AlumniSearch entity, boolean isConnected) {
        AlumniSearchDto dto = new AlumniSearchDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBatch(entity.getBatch());
        dto.setDepartment(entity.getDepartment());
        dto.setProfession(entity.getProfession());
        dto.setCompany(entity.getCompany());
        dto.setLocation(entity.getLocation());
        dto.setAvatar(entity.getAvatar());
        dto.setBio(entity.getBio());
        dto.setConnections(entity.getConnections());
        dto.setIsConnected(isConnected);
        dto.setSkills(entity.getSkills());
        return dto;
    }
}
