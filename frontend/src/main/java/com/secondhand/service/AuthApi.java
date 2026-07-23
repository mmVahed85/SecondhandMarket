package com.secondhand.service;

import com.secondhand.dto.RegisterResponse;
import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.dto.RegisterRequest; // مطمئن شوید کلاس RegisterRequest در مسیر درست import شود
import com.secondhand.util.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;

public class AuthApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<LoginResponse> login(LoginRequest request){

        return apiClient.post(
                "/api/auth/login",
                request,
                new TypeReference<ApiResponse<LoginResponse>>() {}
        );

    }

    public ApiResponse<RegisterResponse> register(RegisterRequest request){

        return apiClient.post(
                "/api/auth/register",
                request,
                new TypeReference<ApiResponse<RegisterResponse>>() {}
        );

    }
}