package com.iitju.ijaa.service;

import com.iitju.ijaa.entity.User;
import com.iitju.ijaa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);


    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public String verify(User user) {
        //TODO: Should I find in the repository for the username first?

        User userExists = userRepository.findByUsername(user.getUsername());

        if(userExists == null) {
            return "Wrong username or password";
        }

        if(!userExists.isActive()) {
            return "User is not active";
        }

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        }
        return "Failed";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
