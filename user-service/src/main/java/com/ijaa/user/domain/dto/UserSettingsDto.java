package com.ijaa.user.domain.dto;

import com.ijaa.user.domain.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDto {
    private String userId;
    private Theme theme;
}




