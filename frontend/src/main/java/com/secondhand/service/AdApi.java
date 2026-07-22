package com.secondhand.service;

import java.util.List;

import com.secondhand.dto.AdvertisementFilterRequest;
import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.util.ApiResponse;

public class AdApi {
    private final ApiClient apiClient = new ApiClient();

    // متد قبلی: برای ثبت آگهی
    public ApiResponse<AdvertisementResponse> createAd(CreateAdvertisementRequest request) {
        return apiClient.post("/api/ads", request, new TypeReference<ApiResponse<AdvertisementResponse>>() {});
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
}