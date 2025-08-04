package com.ijaa.user.domain.enums;

public enum AdminRole {
    USER("USER", "Regular user access"),
    ADMIN("ADMIN", "Administrative access");

    private final String role;
    private final String description;

    AdminRole(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }
} 