package com.ijaa.user.service.impl;

import com.ijaa.user.domain.dto.UserSettingsDto;
import com.ijaa.user.domain.entity.UserSettings;
import com.ijaa.user.domain.enums.Theme;
import com.ijaa.user.domain.request.UserSettingsRequest;
import com.ijaa.user.repository.UserSettingsRepository;
import com.ijaa.user.service.UserSettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;

    @Override
    @Transactional(readOnly = true)
    public UserSettingsDto getUserSettings(String userId) {
        log.info("Getting user settings for userId: {}", userId);
        
        UserSettings userSettings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultSettings(userId));
        
        return convertToDto(userSettings);
    }

    @Override
    @Transactional
    public UserSettingsDto updateUserSettings(String userId, UserSettingsRequest request) {
        log.info("Updating user settings for userId: {}, theme: {}", userId, request.getTheme());
        
        UserSettings userSettings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserSettings defaultSettings = new UserSettings();
                    defaultSettings.setUserId(userId);
                    defaultSettings.setTheme(Theme.DEVICE);
                    return defaultSettings;
                });
        
        userSettings.setTheme(request.getTheme());
        UserSettings savedSettings = userSettingsRepository.save(userSettings);
        
        log.info("User settings updated successfully for userId: {}", userId);
        return convertToDto(savedSettings);
    }

    @Override
    @Transactional(readOnly = true)
    public Theme getThemeForUser(String userId) {
        log.debug("Getting theme for userId: {}", userId);
        
        return userSettingsRepository.findByUserId(userId)
                .map(UserSettings::getTheme)
                .orElse(Theme.DEVICE);
    }

    private UserSettings createDefaultSettings(String userId) {
        log.debug("Creating default settings for userId: {}", userId);
        
        UserSettings defaultSettings = new UserSettings();
        defaultSettings.setUserId(userId);
        defaultSettings.setTheme(Theme.DEVICE);
        
        return userSettingsRepository.save(defaultSettings);
    }

    private UserSettingsDto convertToDto(UserSettings userSettings) {
        return new UserSettingsDto(
                userSettings.getUserId(),
                userSettings.getTheme()
        );
    }
}
