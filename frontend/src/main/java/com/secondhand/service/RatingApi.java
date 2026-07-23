package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.dto.RatingRequest;
import com.secondhand.dto.RatingResponse;
import com.secondhand.util.ApiResponse;

public class RatingApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<RatingResponse> submitRating(
            Long advertisementId,
            RatingRequest request) {

        return apiClient.post(
                "/api/advertisements/" + advertisementId + "/rating",
                request,
                new TypeReference<ApiResponse<RatingResponse>>() {}
        );
    }
}