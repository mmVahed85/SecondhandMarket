package com.secondhand.service;

import com.secondhand.model.User;

public class UserApi {

    private final ApiClient apiClient = new ApiClient();

    public User getTestUser() {

        return apiClient.get(
                "/api/users/test",
                User.class
        );

    }

}