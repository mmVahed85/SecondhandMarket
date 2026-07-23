package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.model.User;
import com.secondhand.util.ApiResponse;

public class UserApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<User> getTestUser() {

        return apiClient.get(
                "/api/users/test",
                new TypeReference<ApiResponse<User>>() {}
        );
    }

}