package com.secondhand.service;

import com.secondhand.model.LoginRequest;
import com.secondhand.model.LoginResponse;

public class AuthApi {

    private final ApiClient apiClient = new ApiClient();

    public LoginResponse login(LoginRequest request){

        return apiClient.post(
                "/api/auth/login",
                request,
                LoginResponse.class
        );

    }

}