package com.ijaa.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.Experience;
import com.ijaa.user.domain.entity.Interest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.repository.ExperienceRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.service.BaseService;
import com.ijaa.user.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfileServiceImpl extends BaseService implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ExperienceRepository experienceRepository;
    private final InterestRepository interestRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ObjectMapper objectMapper,
                              ExperienceRepository experienceRepository,
                              InterestRepository interestRepository) {
        super(objectMapper);
        this.profileRepository = profileRepository;
        this.experienceRepository = experienceRepository;
        this.interestRepository = interestRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getProfileByUserId(String userId) {
        String currentUsername = getCurrentUsername();
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Check if this is the current user viewing their own profile
        boolean isOwnProfile = currentUsername.equals(profile.getUsername());

        return toDto(profile, isOwnProfile);
    }

    @Override
    public ProfileDto updateBasicInfo(ProfileDto dto) {
        String username = getCurrentUsername();
        Profile entity = profileRepository.findByUsername(username)
                .orElseGet(() -> createNewProfile(username));

        updateProfileFields(entity, dto);
        updateVisibilitySettings(entity, dto);

        profileRepository.save(entity);
        return toDto(entity, true);
    }

    @Override
    public ProfileDto updateVisibility(ProfileDto dto) {
        String username = getCurrentUsername();
        Profile entity = profileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        updateVisibilitySettings(entity, dto);

        profileRepository.save(entity);
        return toDto(entity, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceDto> getExperiencesByUserId(String userId) {
        List<Experience> experiences = experienceRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return experiences.stream()
                .map(this::experienceToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExperienceDto addExperience(ExperienceDto experienceDto) {
        String username = getCurrentUsername();
        String userId = getCurrentUserId();

        Experience experience = Experience.builder()
                .username(username)
                .userId(userId)
                .title(experienceDto.getTitle())
                .company(experienceDto.getCompany())
                .period(experienceDto.getPeriod())
                .description(experienceDto.getDescription())
                .build();

        Experience savedExperience = experienceRepository.save(experience);
        return experienceToDto(savedExperience);
    }

    @Override
    public void deleteExperience(String userId) {
        String currentUserId = getCurrentUserId();
        Experience experience = experienceRepository.findByUserIdAndUserId(userId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Experience not found or unauthorized"));

        experienceRepository.delete(experience);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InterestDto> getInterestsByUserId(String userId) {
        List<Interest> interests = interestRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return interests.stream()
                .map(this::interestToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InterestDto addInterest(String interestName) {
        if (interestName == null || interestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Interest cannot be empty");
        }

        String username = getCurrentUsername();
        String userId = getCurrentUserId();
        String cleanedInterest = interestName.trim();

        // Check if interest already exists for this user
        boolean exists = interestRepository.existsByUserIdAndInterestIgnoreCase(userId, cleanedInterest);
        if (exists) {
            throw new IllegalArgumentException("Interest already exists");
        }

        Interest interest = Interest.builder()
                .username(username)
                .userId(userId)
                .interest(cleanedInterest)
                .build();

        Interest savedInterest = interestRepository.save(interest);
        return interestToDto(savedInterest);
    }

    @Override
    public void deleteInterest(String userId) {
        String currentUserId = getCurrentUserId();
        Interest interest = interestRepository.findByUserIdAndUserId(userId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Interest not found or unauthorized"));

        interestRepository.delete(interest);
    }

    // Private helper methods
    private Profile createNewProfile(String username) {
        return profileRepository.save(Profile.builder()
                .username(username)
                .userId(generateUserId())
                .build());
    }

    private void updateProfileFields(Profile entity, ProfileDto dto) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getProfession() != null) entity.setProfession(dto.getProfession());
        if (dto.getLocation() != null) entity.setLocation(dto.getLocation());
        if (dto.getBio() != null) entity.setBio(dto.getBio());
        if (dto.getPhone() != null) entity.setPhone(dto.getPhone());
        if (dto.getLinkedIn() != null) entity.setLinkedIn(dto.getLinkedIn());
        if (dto.getWebsite() != null) entity.setWebsite(dto.getWebsite());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getBatch() != null) entity.setBatch(dto.getBatch());
        if (dto.getFacebook() != null) entity.setFacebook(dto.getFacebook());
    }

    private void updateVisibilitySettings(Profile entity, ProfileDto dto) {
        if (dto.getShowPhone() != null) entity.setShowPhone(dto.getShowPhone());
        if (dto.getShowLinkedIn() != null) entity.setShowLinkedIn(dto.getShowLinkedIn());
        if (dto.getShowWebsite() != null) entity.setShowWebsite(dto.getShowWebsite());
        if (dto.getShowEmail() != null) entity.setShowEmail(dto.getShowEmail());
        if (dto.getShowFacebook() != null) entity.setShowFacebook(dto.getShowFacebook());
    }

    private ProfileDto toDto(Profile entity, boolean includePrivateFields) {
        ProfileDto dto = new ProfileDto();
        dto.setUserId(entity.getUserId());
        dto.setName(entity.getName());
        dto.setProfession(entity.getProfession());
        dto.setLocation(entity.getLocation());
        dto.setBio(entity.getBio());
        dto.setBatch(entity.getBatch());
        dto.setConnections(entity.getConnections());

        // Always include visibility flags
        dto.setShowPhone(entity.getShowPhone());
        dto.setShowLinkedIn(entity.getShowLinkedIn());
        dto.setShowWebsite(entity.getShowWebsite());
        dto.setShowEmail(entity.getShowEmail());
        dto.setShowFacebook(entity.getShowFacebook());

        if (includePrivateFields) {
            // For own profile, include all contact info
            dto.setPhone(entity.getPhone());
            dto.setLinkedIn(entity.getLinkedIn());
            dto.setWebsite(entity.getWebsite());
            dto.setEmail(entity.getEmail());
            dto.setFacebook(entity.getFacebook());
        } else {
            // For other users' profiles, apply privacy settings
            dto.setPhone(Boolean.TRUE.equals(entity.getShowPhone()) ? entity.getPhone() : null);
            dto.setLinkedIn(Boolean.TRUE.equals(entity.getShowLinkedIn()) ? entity.getLinkedIn() : null);
            dto.setWebsite(Boolean.TRUE.equals(entity.getShowWebsite()) ? entity.getWebsite() : null);
            dto.setEmail(Boolean.TRUE.equals(entity.getShowEmail()) ? entity.getEmail() : null);
            dto.setFacebook(Boolean.TRUE.equals(entity.getShowFacebook()) ? entity.getFacebook() : null);
        }

        return dto;
    }

    private ExperienceDto experienceToDto(Experience entity) {
        ExperienceDto dto = new ExperienceDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setTitle(entity.getTitle());
        dto.setCompany(entity.getCompany());
        dto.setPeriod(entity.getPeriod());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private InterestDto interestToDto(Interest entity) {
        InterestDto dto = new InterestDto();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUserId());
        dto.setInterest(entity.getInterest());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    private String generateUserId() {
        // Implement your user ID generation logic here
        return java.util.UUID.randomUUID().toString();
    }

    private String getCurrentUserId() {
        // Implement logic to get current user's ID
        // This should be derived from the current username or security context
        String username = getCurrentUsername();
        return profileRepository.findByUsername(username)
                .map(Profile::getUserId)
                .orElseThrow(() -> new RuntimeException("User profile not found"));
    }
}
