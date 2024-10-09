package com.iitju.ijaa.controller;

import com.iitju.ijaa.dto.ApiResponse;
import com.iitju.ijaa.entity.User;
import com.iitju.ijaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String,Object>>> login(@RequestBody User user) {
        return userService.verify(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


}
