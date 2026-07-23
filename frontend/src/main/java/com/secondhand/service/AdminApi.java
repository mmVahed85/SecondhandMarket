package com.secondhand.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.UserResponse;
import com.secondhand.util.ApiResponse;

public class AdminApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<AdvertisementResponse> approve(Long adId) {

        return apiClient.put("/api/admin/advertisements/" + adId + "/approve", new TypeReference<ApiResponse<AdvertisementResponse>>() {});
    } 

    public ApiResponse<AdvertisementResponse> reject(Long adId) {

        return apiClient.put("/api/admin/advertisements/" + adId + "/reject", new TypeReference<ApiResponse<AdvertisementResponse>>() {});
    } 

    public ApiResponse<AdvertisementResponse> deleteAd(Long adId) {

        return apiClient.delete("/api/admin/advertisements/" + adId, new TypeReference<ApiResponse<AdvertisementResponse>>() {});
    } 

    public ApiResponse<String> deleteUser(Long userId) {

        return apiClient.delete("/api/admin/users/" + userId, new TypeReference<ApiResponse<String>>() {});
    } 

    public ApiResponse<List<AdvertisementResponse>> getPendingAdvertisements() {

        return apiClient.get("/api/admin/advertisements/pending", new TypeReference<ApiResponse<List<AdvertisementResponse>>>() {});
    } 

    public ApiResponse<List<UserResponse>> getUsers() {

        return apiClient.get("/api/admin/users", new TypeReference<ApiResponse<List<UserResponse>>>() {});
    }

    public ApiResponse<String> blockUser(Long userId) {

        return apiClient.put("/api/admin/users/" + userId + "/block", new TypeReference<ApiResponse<String>>() {});
    } 

    public ApiResponse<String> unblockUser(Long userId) {

        return apiClient.put("/api/admin/users/" + userId + "/unblock", new TypeReference<ApiResponse<String>>() {});
    } 
}