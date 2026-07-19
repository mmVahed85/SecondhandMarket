package com.secondhand.service;

import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.dto.RegisterRequest;
import com.secondhand.entity.User;
import com.secondhand.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null){
            throw new RuntimeException("Username already exists");
        }
        else if(userRepository.findByPhone(request.getPhone()) != null){
            throw new RuntimeException("Phone already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());

        try {
            userRepository.save(user);
        }
        catch(Exception e) {
            return "Database error";
        }
        return "User registered successfully";
    }



    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername());

        if(user != null && passwordEncoder.matches(request.getPassword(),user.getPassword())) {
            return new LoginResponse(
                    true,
                    "Login successful",
                    "TOKEN_123456"
            );
        }

        return new LoginResponse(
                false,
                "Wrong username or password",
                ""
        );
    }

}