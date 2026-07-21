package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.service.AuthService;
import com.secondhand.util.ApiResponse;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Null> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {

        return authService.login(request);

    }

}