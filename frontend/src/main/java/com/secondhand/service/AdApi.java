package com.secondhand.service;

import com.secondhand.model.CreateAdRequest;

public class AdApi {
    private final ApiClient apiClient = new ApiClient();

    // متد ارسال آگهی به سرور
    public String createAd(CreateAdRequest request) {
        // فرض می‌کنیم هم‌گروهی شما مسیر /api/ads را برای ثبت آگهی در نظر گرفته است
        // نوع خروجی را String در نظر می‌گیریم تا پاسخ متنی سرور را دریافت کنیم
        return apiClient.post("/api/ads", request, String.class);
    }
}