package com.ijaa.event.presenter.rest.client;

import com.ijaa.event.domain.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{userId}")
    ProfileDto getProfileByUserId(@PathVariable String userId, @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/users/username/{username}")
    ProfileDto getProfileByUsername(@PathVariable String username, @RequestHeader("Authorization") String token);

    @GetMapping("/api/v1/user/validate")
    boolean validateUser(@RequestHeader("Authorization") String token);
}
