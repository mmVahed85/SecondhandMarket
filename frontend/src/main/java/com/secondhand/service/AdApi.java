package com.secondhand.service;

import java.io.File;
import java.util.List;

import com.secondhand.dto.AdvertisementFilterRequest;
import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import com.secondhand.dto.ImageResponse;
import com.secondhand.dto.UpdateAdvertisementRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.util.ApiResponse;

public class AdApi {
    private final ApiClient apiClient = new ApiClient();

    // متد قبلی: برای ثبت آگهی
    public ApiResponse<AdvertisementResponse> createAd(CreateAdvertisementRequest request) {
        return apiClient.post("/api/advertisements", request, new TypeReference<ApiResponse<AdvertisementResponse>>() {});
    }

    // متد جدید: برای دریافت لیست آگهی‌ها
    public ApiResponse<List<AdvertisementResponse>> getActiveAds() {
        try {
            // فرض می‌کنیم هم‌گروهی شما مسیر /api/ads را برای گرفتن آگهی‌های فعال تنظیم کرده است
            // خروجی را به صورت یک آرایه از کلاس Ad دریافت می‌کنیم
            return apiClient.get("/api/advertisements", new TypeReference<ApiResponse<List<AdvertisementResponse>>>() {});
        } catch (Exception e) {
            System.err.println("خطا در دریافت لیست آگهی‌ها:");
            e.printStackTrace();
            return new ApiResponse<List<AdvertisementResponse>>(); // در صورت خطا، یک لیست خالی برمی‌گرداند تا برنامه کرش نکند
        }
    }

    public ApiResponse<List<AdvertisementResponse>> filterAdvertisements(AdvertisementFilterRequest request) {
        try {
            // فرض می‌کنیم هم‌گروهی شما مسیر /api/ads را برای گرفتن آگهی‌های فعال تنظیم کرده است
            // خروجی را به صورت یک آرایه از کلاس Ad دریافت می‌کنیم
            return apiClient.post("/api/advertisements/search", request, new TypeReference<ApiResponse<List<AdvertisementResponse>>>() {});
        } catch (Exception e) {
            System.err.println("خطا در دریافت لیست آگهی‌ها:");
            e.printStackTrace();
            return new ApiResponse<List<AdvertisementResponse>>(); // در صورت خطا، یک لیست خالی برمی‌گرداند تا برنامه کرش نکند
        }
    }

    public ApiResponse<ImageResponse> uploadImage(Long adId, File image) {

        return apiClient.multipart(
                "/api/advertisements/" + adId + "/images",
                "image",
                image,
                new TypeReference<ApiResponse<ImageResponse>>() {}
        );

    }

    public ApiResponse<String> deleteImage(Long imageId) {

        return apiClient.delete(
                "/api/advertisements/images/" + imageId,
                new TypeReference<ApiResponse<String>>() {}
        );

    }

    public ApiResponse<AdvertisementResponse> updateAd(long adId, UpdateAdvertisementRequest request) {

        return apiClient.put(
                "/api/advertisements/" + adId,
                request,
                new TypeReference<ApiResponse<AdvertisementResponse>>() {}
        );
    }

    public ApiResponse<AdvertisementResponse> getAdvertisement(long adId) {

        return apiClient.get(
                "/api/advertisements/" + adId,
                new TypeReference<ApiResponse<AdvertisementResponse>>() {}
        );
    }

    public ApiResponse<List<AdvertisementResponse>> getMyAds() {

        return apiClient.get("/api/advertisements/my", new TypeReference<ApiResponse<List<AdvertisementResponse>>>() {});
    }

    public ApiResponse<AdvertisementResponse> delete(Long adId) {

        return apiClient.delete("/api/advertisements/" + adId, new TypeReference<ApiResponse<AdvertisementResponse>>() {});
    }

    public ApiResponse<List<AdvertisementResponse>> getMyFavorites() {

        return apiClient.get("/api/advertisements/me/favorites", new TypeReference<ApiResponse<List<AdvertisementResponse>>>() {});
    }

    public ApiResponse<AdvertisementResponse> deleteFavorite(Long adId) {

        return apiClient.delete("/api/advertisements/" + adId + "/favorite", new TypeReference<ApiResponse<AdvertisementResponse>>() {});
    }

    public ApiResponse<String> addFavorite(Long adId) {

        return apiClient.post("/api/advertisements/" + adId + "/favorite", new TypeReference<ApiResponse<String>>() {});
    }
}