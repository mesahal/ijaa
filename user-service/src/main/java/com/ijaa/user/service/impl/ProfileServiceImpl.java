package com.ijaa.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.CityDto;
import com.ijaa.user.domain.dto.CountryDto;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.Experience;
import com.ijaa.user.domain.entity.Interest;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.domain.entity.User;
import com.ijaa.user.repository.ExperienceRepository;
import com.ijaa.user.repository.InterestRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.repository.UserRepository;
import com.ijaa.user.service.BaseService;
import com.ijaa.user.service.LocationService;
import com.ijaa.user.service.ProfileService;
import com.ijaa.user.common.utils.UniqueIdGenerator;
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
    private final UserRepository userRepository;
    private final LocationService locationService;
    private final UniqueIdGenerator uniqueIdGenerator;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              ObjectMapper objectMapper,
                              ExperienceRepository experienceRepository,
                              InterestRepository interestRepository,
                              UserRepository userRepository,
                              LocationService locationService,
                              UniqueIdGenerator uniqueIdGenerator) {
        super(objectMapper);
        this.profileRepository = profileRepository;
        this.experienceRepository = experienceRepository;
        this.interestRepository = interestRepository;
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDto getProfileByUserId(String userId) {
        String currentUserId = getCurrentUserId();
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Check if this is the current user viewing their own profile
        boolean isOwnProfile = currentUserId.equals(profile.getUserId());

        return toDto(profile, isOwnProfile);
    }

    @Override
    public ProfileDto updateBasicInfo(ProfileDto dto) {
        String username = getCurrentUsername();
        String userId = getCurrentUserId();
        Profile entity = profileRepository.findByUserId(userId)
                .orElseGet(() -> createNewProfile(username, userId));

        updateProfileFields(entity, dto);
        updateVisibilitySettings(entity, dto);

        profileRepository.save(entity);
        return toDto(entity, true);
    }

    @Override
    public ProfileDto updateVisibility(ProfileDto dto) {
        String userId = getCurrentUserId();
        Profile entity = profileRepository.findByUserId(userId)
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

        Experience experience = new Experience();
        experience.setUsername(username);
        experience.setUserId(userId);
        experience.setTitle(experienceDto.getTitle());
        experience.setCompany(experienceDto.getCompany());
        experience.setPeriod(experienceDto.getPeriod());
        experience.setDescription(experienceDto.getDescription());

        Experience savedExperience = experienceRepository.save(experience);
        return experienceToDto(savedExperience);
    }

    @Override
    public void deleteExperience(Long experienceId) {
        String currentUserId = getCurrentUserId();
        Experience experience = experienceRepository.findByIdAndUserId(experienceId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Experience not found or unauthorized"));

        experienceRepository.delete(experience);
    }

    @Override
    public ExperienceDto updateExperience(Long experienceId, ExperienceDto experienceDto) {
        String currentUserId = getCurrentUserId();
        Experience experience = experienceRepository.findByIdAndUserId(experienceId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Experience not found or unauthorized"));

        // Update experience fields
        if (experienceDto.getTitle() != null) {
            experience.setTitle(experienceDto.getTitle());
        }
        if (experienceDto.getCompany() != null) {
            experience.setCompany(experienceDto.getCompany());
        }
        if (experienceDto.getPeriod() != null) {
            experience.setPeriod(experienceDto.getPeriod());
        }
        if (experienceDto.getDescription() != null) {
            experience.setDescription(experienceDto.getDescription());
        }

        Experience updatedExperience = experienceRepository.save(experience);
        return experienceToDto(updatedExperience);
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

        Interest interest = new Interest();
        interest.setUsername(username);
        interest.setUserId(userId);
        interest.setInterest(cleanedInterest);

        Interest savedInterest = interestRepository.save(interest);
        return interestToDto(savedInterest);
    }

    @Override
    public void deleteInterest(Long interestId) {
        String currentUserId = getCurrentUserId();
        Interest interest = interestRepository.findByIdAndUserId(interestId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Interest not found or unauthorized"));

        interestRepository.delete(interest);
    }

    @Override
    public InterestDto updateInterest(Long interestId, String interestName) {
        if (interestName == null || interestName.trim().isEmpty()) {
            throw new IllegalArgumentException("Interest cannot be empty");
        }

        String currentUserId = getCurrentUserId();
        Interest interest = interestRepository.findByIdAndUserId(interestId, currentUserId)
                .orElseThrow(() -> new RuntimeException("Interest not found or unauthorized"));

        String cleanedInterest = interestName.trim();

        // Check if the new interest name already exists for this user (excluding current interest)
        boolean exists = interestRepository.existsByUserIdAndInterestIgnoreCase(currentUserId, cleanedInterest);
        if (exists && !cleanedInterest.equalsIgnoreCase(interest.getInterest())) {
            throw new IllegalArgumentException("Interest already exists");
        }

        interest.setInterest(cleanedInterest);
        Interest updatedInterest = interestRepository.save(interest);
        return interestToDto(updatedInterest);
    }

    // Private helper methods
    private Profile createNewProfile(String username, String userId) {
        Profile profile = new Profile();
        profile.setUsername(username);
        profile.setName(username);
        profile.setUserId(userId);
        return profileRepository.save(profile);
    }

    private void updateProfileFields(Profile entity, ProfileDto dto) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getProfession() != null) entity.setProfession(dto.getProfession());
        // Location is now handled via cityId and countryId
        if (dto.getCityId() != null) entity.setCityId(dto.getCityId());
        if (dto.getCountryId() != null) entity.setCountryId(dto.getCountryId());
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
        dto.setCityId(entity.getCityId());
        dto.setCountryId(entity.getCountryId());
        dto.setBio(entity.getBio());
        dto.setBatch(entity.getBatch());
        dto.setConnections(entity.getConnections());
        
        // Note: Location names are no longer populated automatically
        // Clients can use the location APIs to get names by IDs if needed

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
        // Use the centralized UniqueIdGenerator for consistent ID generation
        return uniqueIdGenerator.generateUUID();
    }
}
