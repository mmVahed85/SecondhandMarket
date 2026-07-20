package com.secondhand.service;

import com.secondhand.dto.*;
import com.secondhand.entity.User;
import com.secondhand.entity.Role;
import com.secondhand.repository.UserRepository;
import com.secondhand.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null){
            return new RegisterResponse(false, "Username already exists");
        }
        else if(userRepository.findByPhone(request.getPhone()) != null){
            return new RegisterResponse(false, "Phone already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole(Role.USER);

        try {
            userRepository.save(user);
        }
        catch(Exception e) {
            return new RegisterResponse(false, "Database error");
        }
        return new RegisterResponse(true,"User registered successfully");
    }



    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername());

        if(user != null && passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            if(user.isEnabled()) {
                String token = jwtService.generateToken(user.getUsername());
                return new LoginResponse(true,"Login successful",token, user.getRole().name());
            }
            else {
                return new LoginResponse(false,"Your account has been blocked","", user.getRole().name());
            }
        }
        return new LoginResponse(false,"Wrong username or password","", user.getRole().name());
    }

}