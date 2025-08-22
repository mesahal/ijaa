package com.ijaa.event_service.presenter.rest.client;

import com.ijaa.event_service.domain.dto.ProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/v1/user/profile/{username}")
    ProfileDto getProfileByUsername(@PathVariable String username, @RequestHeader("Authorization") String token);



    @GetMapping("/api/v1/user/validate")
    boolean validateUser(@RequestHeader("Authorization") String token);
}
