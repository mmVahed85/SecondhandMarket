package com.secondhand.service;

import com.secondhand.model.Ad;
import com.secondhand.model.CreateAdRequest;

public class AdApi {
    private final ApiClient apiClient = new ApiClient();

    // متد قبلی: برای ثبت آگهی
    public String createAd(CreateAdRequest request) {
        return apiClient.post("/api/ads", request, String.class);
    }

    // متد جدید: برای دریافت لیست آگهی‌ها
    public Ad[] getActiveAds() {
        try {
            // فرض می‌کنیم هم‌گروهی شما مسیر /api/ads را برای گرفتن آگهی‌های فعال تنظیم کرده است
            // خروجی را به صورت یک آرایه از کلاس Ad دریافت می‌کنیم
            return apiClient.get("/api/ads", Ad[].class);
        } catch (Exception e) {
            System.err.println("خطا در دریافت لیست آگهی‌ها:");
            e.printStackTrace();
            return new Ad[0]; // در صورت خطا، یک لیست خالی برمی‌گرداند تا برنامه کرش نکند
        }
    }
}