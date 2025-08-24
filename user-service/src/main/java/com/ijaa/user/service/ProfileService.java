package com.ijaa.user.service;

import com.ijaa.user.domain.dto.ExperienceDto;
import com.ijaa.user.domain.dto.InterestDto;
import com.ijaa.user.domain.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    // Profile methods
    ProfileDto getProfileByUserId(String userId);
    ProfileDto updateBasicInfo(ProfileDto profileDto);
    ProfileDto updateVisibility(ProfileDto profileDto);

    // Experience methods
    List<ExperienceDto> getExperiencesByUserId(String userId);
    ExperienceDto addExperience(ExperienceDto experienceDto);
    ExperienceDto updateExperience(Long experienceId, ExperienceDto experienceDto);
    void deleteExperience(Long experienceId);

    // Interest methods
    List<InterestDto> getInterestsByUserId(String userId);
    InterestDto addInterest(String interest);
    InterestDto updateInterest(Long interestId, String interestName);
    void deleteInterest(Long interestId);
}
