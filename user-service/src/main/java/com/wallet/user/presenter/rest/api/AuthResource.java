package com.wallet.user.presenter.rest.api;

import com.wallet.user.common.utils.AppUtils;
import com.wallet.user.domain.common.ApiResponse;
import com.wallet.user.domain.entity.User;
import com.wallet.user.domain.request.SignInRequest;
import com.wallet.user.domain.request.SignUpRequest;
import com.wallet.user.domain.response.AuthResponse;
import com.wallet.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(AppUtils.BASE_URL)
public class AuthResource {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<AuthResponse>> signIn(
            @Valid @RequestBody SignInRequest request) {
        String token = authService.verify(request);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(
                new ApiResponse<>("Login successful", "200", authResponse)
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(
            @Valid @RequestBody SignUpRequest request) {
        String token = authService.registerUser(request);
        AuthResponse authResponse = new AuthResponse(token);
        return ResponseEntity.ok(
                new ApiResponse<>("Registration successful", "201", authResponse)
        );
    }

}
