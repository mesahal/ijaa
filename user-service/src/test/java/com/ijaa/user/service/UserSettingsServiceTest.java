package com.ijaa.user.service;

import com.ijaa.user.domain.dto.UserSettingsDto;
import com.ijaa.user.domain.entity.UserSettings;
import com.ijaa.user.domain.enums.Theme;
import com.ijaa.user.domain.request.UserSettingsRequest;
import com.ijaa.user.repository.UserSettingsRepository;
import com.ijaa.user.service.impl.UserSettingsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSettingsServiceTest {

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @InjectMocks
    private UserSettingsServiceImpl userSettingsService;

    private static final String TEST_USER_ID = "test-user-123";
    private UserSettings existingUserSettings;
    private UserSettingsDto expectedDto;

    @BeforeEach
    void setUp() {
        existingUserSettings = new UserSettings();
        existingUserSettings.setId(1L);
        existingUserSettings.setUserId(TEST_USER_ID);
        existingUserSettings.setTheme(Theme.DARK);

        expectedDto = new UserSettingsDto(TEST_USER_ID, Theme.DARK);
    }

    @Test
    void getUserSettings_WhenSettingsExist_ShouldReturnSettings() {
        // Given
        when(userSettingsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.of(existingUserSettings));

        // When
        UserSettingsDto result = userSettingsService.getUserSettings(TEST_USER_ID);

        // Then
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(Theme.DARK, result.getTheme());
        verify(userSettingsRepository).findByUserId(TEST_USER_ID);
        verify(userSettingsRepository, never()).save(any());
    }

    @Test
    void getUserSettings_WhenSettingsDoNotExist_ShouldCreateDefaultSettings() {
        // Given
        when(userSettingsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.empty());
        
        UserSettings defaultSettings = new UserSettings();
        defaultSettings.setUserId(TEST_USER_ID);
        defaultSettings.setTheme(Theme.DEVICE);
        
        when(userSettingsRepository.save(any(UserSettings.class)))
                .thenReturn(defaultSettings);

        // When
        UserSettingsDto result = userSettingsService.getUserSettings(TEST_USER_ID);

        // Then
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(Theme.DEVICE, result.getTheme());
        verify(userSettingsRepository).findByUserId(TEST_USER_ID);
        verify(userSettingsRepository).save(any(UserSettings.class));
    }

    @Test
    void updateUserSettings_WhenSettingsExist_ShouldUpdateSettings() {
        // Given
        UserSettingsRequest request = new UserSettingsRequest(Theme.LIGHT);
        when(userSettingsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.of(existingUserSettings));
        
        UserSettings updatedSettings = new UserSettings();
        updatedSettings.setId(1L);
        updatedSettings.setUserId(TEST_USER_ID);
        updatedSettings.setTheme(Theme.LIGHT);
        
        when(userSettingsRepository.save(any(UserSettings.class)))
                .thenReturn(updatedSettings);

        // When
        UserSettingsDto result = userSettingsService.updateUserSettings(TEST_USER_ID, request);

        // Then
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(Theme.LIGHT, result.getTheme());
        verify(userSettingsRepository).findByUserId(TEST_USER_ID);
        verify(userSettingsRepository).save(any(UserSettings.class));
    }

    @Test
    void updateUserSettings_WhenSettingsDoNotExist_ShouldCreateNewSettings() {
        // Given
        UserSettingsRequest request = new UserSettingsRequest(Theme.LIGHT);
        when(userSettingsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.empty());
        
        UserSettings newSettings = new UserSettings();
        newSettings.setUserId(TEST_USER_ID);
        newSettings.setTheme(Theme.LIGHT);
        
        when(userSettingsRepository.save(any(UserSettings.class)))
                .thenReturn(newSettings);

        // When
        UserSettingsDto result = userSettingsService.updateUserSettings(TEST_USER_ID, request);

        // Then
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result.getUserId());
        assertEquals(Theme.LIGHT, result.getTheme());
        verify(userSettingsRepository).findByUserId(TEST_USER_ID);
        verify(userSettingsRepository).save(any(UserSettings.class));
    }

    @Test
    void getThemeForUser_WhenSettingsExist_ShouldReturnTheme() {
        // Given
        when(userSettingsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.of(existingUserSettings));

        // When
        Theme result = userSettingsService.getThemeForUser(TEST_USER_ID);

        // Then
        assertEquals(Theme.DARK, result);
        verify(userSettingsRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void getThemeForUser_WhenSettingsDoNotExist_ShouldReturnDefaultTheme() {
        // Given
        when(userSettingsRepository.findByUserId(TEST_USER_ID))
                .thenReturn(Optional.empty());

        // When
        Theme result = userSettingsService.getThemeForUser(TEST_USER_ID);

        // Then
        assertEquals(Theme.DEVICE, result);
        verify(userSettingsRepository).findByUserId(TEST_USER_ID);
    }

    @Test
    void updateUserSettings_WithAllThemeOptions_ShouldWorkCorrectly() {
        // Test all theme options
        Theme[] themes = {Theme.DARK, Theme.LIGHT, Theme.DEVICE};
        
        for (Theme theme : themes) {
            // Given
            UserSettingsRequest request = new UserSettingsRequest(theme);
            when(userSettingsRepository.findByUserId(TEST_USER_ID))
                    .thenReturn(Optional.of(existingUserSettings));
            
            UserSettings updatedSettings = new UserSettings();
            updatedSettings.setId(1L);
            updatedSettings.setUserId(TEST_USER_ID);
            updatedSettings.setTheme(theme);
            
            when(userSettingsRepository.save(any(UserSettings.class)))
                    .thenReturn(updatedSettings);

            // When
            UserSettingsDto result = userSettingsService.updateUserSettings(TEST_USER_ID, request);

            // Then
            assertNotNull(result);
            assertEquals(TEST_USER_ID, result.getUserId());
            assertEquals(theme, result.getTheme());
        }
    }

    @Test
    void getUserSettings_ShouldHandleNullUserId() {
        // Given
        when(userSettingsRepository.findByUserId(null))
                .thenReturn(Optional.empty());
        
        UserSettings defaultSettings = new UserSettings();
        defaultSettings.setUserId(null);
        defaultSettings.setTheme(Theme.DEVICE);
        
        when(userSettingsRepository.save(any(UserSettings.class)))
                .thenReturn(defaultSettings);

        // When
        UserSettingsDto result = userSettingsService.getUserSettings(null);

        // Then
        assertNotNull(result);
        assertNull(result.getUserId());
        assertEquals(Theme.DEVICE, result.getTheme());
    }

    @Test
    void updateUserSettings_ShouldHandleNullUserId() {
        // Given
        UserSettingsRequest request = new UserSettingsRequest(Theme.DARK);
        when(userSettingsRepository.findByUserId(null))
                .thenReturn(Optional.empty());
        
        UserSettings newSettings = new UserSettings();
        newSettings.setUserId(null);
        newSettings.setTheme(Theme.DARK);
        
        when(userSettingsRepository.save(any(UserSettings.class)))
                .thenReturn(newSettings);

        // When
        UserSettingsDto result = userSettingsService.updateUserSettings(null, request);

        // Then
        assertNotNull(result);
        assertNull(result.getUserId());
        assertEquals(Theme.DARK, result.getTheme());
    }
}





