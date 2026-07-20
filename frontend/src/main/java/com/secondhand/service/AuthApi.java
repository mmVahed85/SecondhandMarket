package com.secondhand.service;

import com.secondhand.dto.*;

public class AuthApi {

    private final ApiClient apiClient = new ApiClient();

    public LoginResponse login(LoginRequest request){
        return apiClient.post(
                "/api/auth/login",
                request,
                LoginResponse.class
        );
    }

    // این متد جدید برای ثبت‌نام است
    public RegisterResponse register(RegisterRequest request){
        return apiClient.post(
                "/api/auth/register",
                request,
                RegisterResponse.class
        );
    }
}