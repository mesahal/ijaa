package com.iitju.ijaa.service;

import com.iitju.ijaa.dto.ApiResponse;
import com.iitju.ijaa.entity.User;
import com.iitju.ijaa.repository.UserRepository;
import com.iitju.ijaa.utils.ResponseUtils;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);


    public String register(User user) {
        User userExists = userRepository.findByUsername(user.getUsername());

        if (userExists != null) {
            return "username already exists";
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return "Successfully registered";
    }

    public ResponseEntity<ApiResponse<Map<String,Object>>> verify(User user) {

        User userExists = userRepository.findByUsername(user.getUsername());

        if(userExists == null) {
            return ResponseUtils.createHttpResponse(HttpStatus.OK,"Wrong username or password",null);
        }

        if(!userExists.isActive()) {
            return ResponseUtils.createHttpResponse(HttpStatus.OK,"User is not active",null);
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        String token = jwtService.generateToken(user.getUsername());

        if (authentication.isAuthenticated()) {
            Map<String,Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("user", userExists);
            return ResponseUtils.createHttpResponse(HttpStatus.OK,"Login Successful",responseData);
        }
        return ResponseUtils.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR,"Login failed due to an unexpected error",null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
