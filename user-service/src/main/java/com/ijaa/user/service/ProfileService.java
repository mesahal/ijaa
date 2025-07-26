package com.ijaa.user.service;


import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.ProfileDto;

import java.util.List;

public interface ProfileService {
    ProfileDto getProfile();
    ProfileDto updateBasicInfo(ProfileDto dto);
    ProfileDto updateVisibility(ProfileDto dto);
    List<ExperienceDto> getExperiences();
    ExperienceDto addExperience(ExperienceDto experienceDto);
    void deleteExperience(Long id);
}
