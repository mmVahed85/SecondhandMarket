package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.dto.ApiResponse;
import com.secondhand.model.User;

public class UserApi {

    private final ApiClient apiClient = new ApiClient();

    public User getTestUser() {

        return apiClient.get(
                "/api/users/test",
                new TypeReference<User>() {}
        );

    }

}