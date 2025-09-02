package com.ijaa.user.domain.request;

import com.ijaa.user.domain.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsRequest {
    private Theme theme;
}




