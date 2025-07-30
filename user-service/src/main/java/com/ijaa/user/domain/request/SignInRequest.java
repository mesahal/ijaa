package com.ijaa.user.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {
    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 100)
    private String password;
}
