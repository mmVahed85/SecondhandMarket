package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.dto.*;

public class AuthApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<LoginResponse> login(LoginRequest request){
        return apiClient.post(
                "/api/auth/login",
                request,
                new TypeReference<ApiResponse<LoginResponse>>() {}
        );
    }

    // این متد جدید برای ثبت‌نام است
    public ApiResponse<RegisterResponse> register(RegisterRequest request){
        return apiClient.post(
                "/api/auth/register",
                request,
                new TypeReference<ApiResponse<RegisterResponse>>() {}
        );
    }
}