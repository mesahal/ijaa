package com.ijaa.user.service;

import com.ijaa.user.domain.dto.UserSettingsDto;
import com.ijaa.user.domain.enums.Theme;
import com.ijaa.user.domain.request.UserSettingsRequest;

public interface UserSettingsService {
    UserSettingsDto getUserSettings(String userId);
    UserSettingsDto updateUserSettings(String userId, UserSettingsRequest request);
    Theme getThemeForUser(String userId);
}





