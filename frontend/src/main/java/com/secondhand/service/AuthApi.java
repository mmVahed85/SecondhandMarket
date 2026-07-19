package com.secondhand.service;

import com.secondhand.model.LoginRequest;
import com.secondhand.model.LoginResponse;
import com.secondhand.model.RegisterRequest; // مطمئن شوید کلاس RegisterRequest در مسیر درست import شود

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
    public String register(RegisterRequest request){
        return apiClient.post(
                "/api/auth/register",
                request,
                String.class
        );
    }
}