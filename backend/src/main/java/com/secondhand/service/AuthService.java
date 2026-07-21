package com.secondhand.service;

import com.secondhand.dto.*;
import com.secondhand.entity.User;
import com.secondhand.entity.Role;
import com.secondhand.repository.UserRepository;
import com.secondhand.security.JwtService;
import com.secondhand.util.ApiResponse;

import javax.validation.constraints.Null;

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

    public ApiResponse<Null> register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null){
            return new ApiResponse<>(false,"Username already exists",null);
        }
        else if(userRepository.findByPhone(request.getPhone()) != null){
            return new ApiResponse<>(false,"Phone already exists",null);
        }
        else if(userRepository.findByEmail(request.getEmail()) != null){
            return new ApiResponse<>(false,"Email already exists",null);
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
            return new ApiResponse<>(false,"Database error",null);
        }
        return new ApiResponse<>(true,"User registered successfully",null);
    }



    public ApiResponse<LoginResponse> login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername());

        if(user != null && passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            if(user.isEnabled()) {
                String token = jwtService.generateToken(user.getUsername());
                return new ApiResponse<>(true,"Login successful",new LoginResponse(token, user.getRole().name()));
            }
            else {
                return new ApiResponse<>(false,"Your account has been blocked",null);
            }
        }
        return new ApiResponse<>(false,"Wrong username or password",null);
    }

}