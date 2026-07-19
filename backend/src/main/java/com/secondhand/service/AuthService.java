package com.secondhand.service;

import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String register(RegisterRequest request) {

        System.out.println("Register : " + request.getUsername());

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {

        if (request.getUsername().equals("admin") && request.getPassword().equals("123456")) {
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