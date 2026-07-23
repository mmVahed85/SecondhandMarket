package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.model.ApiResponse;
import com.secondhand.model.LoginRequest;
import com.secondhand.model.LoginResponse;
import com.secondhand.model.RegisterRequest;

public class AuthApi {

    private final ApiClient apiClient = new ApiClient();

    public LoginResponse login(LoginRequest request) {
        // دریافت پاسخ بسته‌بندی شده از سرور
        ApiResponse<LoginResponse> response = apiClient.post(
                "/api/auth/login",
                request,
                new TypeReference<ApiResponse<LoginResponse>>() {}
        );

        if (response != null && response.isSuccess()) {
            return response.getData(); // برگرداندن توکن و رول
        }

        throw new RuntimeException(response != null ? response.getMessage() : "خطا در برقراری ارتباط");
    }

    public String register(RegisterRequest request) {
        ApiResponse<Void> response = apiClient.post(
                "/api/auth/register",
                request,
                new TypeReference<ApiResponse<Void>>() {}
        );

        if (response != null && response.isSuccess()) {
            return response.getMessage();
        }

        throw new RuntimeException(response != null ? response.getMessage() : "خطا در ثبت‌نام");
    }
}