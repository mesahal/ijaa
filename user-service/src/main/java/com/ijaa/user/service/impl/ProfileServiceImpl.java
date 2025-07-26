package com.ijaa.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.ProfileDto;
import com.ijaa.user.domain.entity.Experience;
import com.ijaa.user.domain.entity.Profile;
import com.ijaa.user.repository.ExperienceRepository;
import com.ijaa.user.repository.ProfileRepository;
import com.ijaa.user.service.BaseService;
import com.ijaa.user.service.ProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl extends BaseService implements ProfileService {


    public ProfileServiceImpl(ProfileRepository repository, ObjectMapper objectMapper, ExperienceRepository experienceRepository) {
        super(objectMapper);
        this.repository = repository;
        this.experienceRepository = experienceRepository;
    }

    private final ProfileRepository repository;
    private final ExperienceRepository experienceRepository;

    @Override
    public ProfileDto getProfile() {
        String username = getCurrentUsername();
        Profile profile = repository.findByUsername(username)
                .orElseGet(() -> repository.save(Profile.builder().username(username).build()));
        return toDto(profile);
    }

    @Override
    public ProfileDto updateBasicInfo(ProfileDto dto) {
        String username = getCurrentUsername();
        Profile entity = repository.findByUsername(username)
                .orElseGet(() -> repository.save(Profile.builder().username(username).build()));

        // Update basic profile fields
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getProfession() != null) {
            entity.setProfession(dto.getProfession());
        }
        if (dto.getLocation() != null) {
            entity.setLocation(dto.getLocation());
        }
        if (dto.getBio() != null) {
            entity.setBio(dto.getBio());
        }
        if (dto.getPhone() != null) {
            entity.setPhone(dto.getPhone());
        }
        if (dto.getLinkedIn() != null) {
            entity.setLinkedIn(dto.getLinkedIn());
        }
        if (dto.getWebsite() != null) {
            entity.setWebsite(dto.getWebsite());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getBatch() != null) {
            entity.setBatch(dto.getBatch());
        }
        if (dto.getDepartment() != null) {
            entity.setDepartment(dto.getDepartment());
        }

        // Update visibility settings if provided
        if (dto.getShowPhone() != null) {
            entity.setShowPhone(dto.getShowPhone());
        }
        if (dto.getShowLinkedIn() != null) {
            entity.setShowLinkedIn(dto.getShowLinkedIn());
        }
        if (dto.getShowWebsite() != null) {
            entity.setShowWebsite(dto.getShowWebsite());
        }
        if (dto.getShowEmail() != null) {
            entity.setShowEmail(dto.getShowEmail());
        }

        repository.save(entity);
        return toDto(entity);
    }

    @Override
    public ProfileDto updateVisibility(ProfileDto dto) {
        String username = getCurrentUsername();
        Profile entity = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Only update visibility fields
        if (dto.getShowPhone() != null) {
            entity.setShowPhone(dto.getShowPhone());
        }
        if (dto.getShowLinkedIn() != null) {
            entity.setShowLinkedIn(dto.getShowLinkedIn());
        }
        if (dto.getShowWebsite() != null) {
            entity.setShowWebsite(dto.getShowWebsite());
        }
        if (dto.getShowEmail() != null) {
            entity.setShowEmail(dto.getShowEmail());
        }

        repository.save(entity);
        return toDto(entity);
    }

    private ProfileDto toDto(Profile entity) {
        ProfileDto dto = new ProfileDto();
        dto.setName(entity.getName());
        dto.setProfession(entity.getProfession());
        dto.setLocation(entity.getLocation());
        dto.setBio(entity.getBio());
        dto.setPhone(entity.getPhone());
        dto.setLinkedIn(entity.getLinkedIn());
        dto.setWebsite(entity.getWebsite());
        dto.setEmail(entity.getEmail());
        dto.setBatch(entity.getBatch());
        dto.setDepartment(entity.getDepartment());
        dto.setShowPhone(entity.getShowPhone());
        dto.setShowLinkedIn(entity.getShowLinkedIn());
        dto.setShowWebsite(entity.getShowWebsite());
        dto.setShowEmail(entity.getShowEmail());
        return dto;
    }

    @Override
    public List<ExperienceDto> getExperiences() {
        String username = getCurrentUsername();
        List<Experience> experiences = experienceRepository.findByUsernameOrderByCreatedAtDesc(username);
        return experiences.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExperienceDto addExperience(ExperienceDto experienceDto) {
        String username = getCurrentUsername();

        Experience experience = new Experience();
        experience.setUsername(username);
        experience.setTitle(experienceDto.getTitle());
        experience.setCompany(experienceDto.getCompany());
        experience.setPeriod(experienceDto.getPeriod());
        experience.setDescription(experienceDto.getDescription());

        Experience savedExperience = experienceRepository.save(experience);
        return toDto(savedExperience);
    }

    @Override
    @Transactional
    public void deleteExperience(Long id) {
        String username = getCurrentUsername();
        experienceRepository.deleteByIdAndUsername(id, username);
    }

    private ExperienceDto toDto(Experience entity) {
        ExperienceDto dto = new ExperienceDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCompany(entity.getCompany());
        dto.setPeriod(entity.getPeriod());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
