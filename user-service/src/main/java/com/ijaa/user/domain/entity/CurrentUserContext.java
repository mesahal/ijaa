package com.ijaa.user.domain.entity;

import lombok.Data;

@Data
public class CurrentUserContext {
    private String username;
    private String userId;
    private String userType;
    private String role;
}
