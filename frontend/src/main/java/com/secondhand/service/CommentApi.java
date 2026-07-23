package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.dto.CommentResponse;
import com.secondhand.dto.CreateCommentRequest;
import com.secondhand.util.ApiResponse;

import java.util.List;

public class CommentApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<CommentResponse> createComment(
            Long advertisementId,
            CreateCommentRequest request) {

        return apiClient.post(
                "/api/advertisements/" + advertisementId + "/comments",
                request,
                new TypeReference<ApiResponse<CommentResponse>>() {}
        );
    }

    public ApiResponse<List<CommentResponse>> getComments(
            Long advertisementId) {

        return apiClient.get(
                "/api/advertisements/" + advertisementId + "/comments",
                new TypeReference<ApiResponse<List<CommentResponse>>>() {}
        );
    }
}