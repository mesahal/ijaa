package com.ijaa.user.service;

import com.ijaa.user.domain.entity.CurrentUserContext;

public interface UserContextService {
    CurrentUserContext getCurrentUserContext();
    String getCurrentUsername();
    String getCurrentUserId();
    boolean hasUserContext();
}


