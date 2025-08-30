package com.ijaa.user.domain.enums;

public enum AuthProvider {
    LOCAL("LOCAL", "Local authentication"),
    GOOGLE("GOOGLE", "Google OAuth2 authentication");

    private final String provider;
    private final String description;

    AuthProvider(String provider, String description) {
        this.provider = provider;
        this.description = description;
    }

    public String getProvider() {
        return provider;
    }

    public String getDescription() {
        return description;
    }
}
