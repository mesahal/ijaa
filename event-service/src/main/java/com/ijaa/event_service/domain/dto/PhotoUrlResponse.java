package com.ijaa.event_service.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUrlResponse {
    private String photoUrl;
    private String message;
    private Boolean exists;
}
