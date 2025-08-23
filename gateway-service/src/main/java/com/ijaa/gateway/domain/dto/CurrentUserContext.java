package com.ijaa.gateway.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CurrentUserContext implements Serializable {
    private String username;
    private String userId;
    private String userType;
    private String role;
}
