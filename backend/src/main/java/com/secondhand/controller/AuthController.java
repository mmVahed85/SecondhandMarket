package com.secondhand.controller;

import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.dto.RegisterRequest;
import com.secondhand.service.AuthService;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        return authService.login(request);

    }

}