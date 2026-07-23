package com.secondhand.service;

import com.secondhand.model.ApiResponse;
import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import java.util.List;
import java.util.Arrays;

public class AdApi {
    private final ApiClient apiClient = new ApiClient();

    public AdvertisementResponse createAd(CreateAdvertisementRequest request) {
        try {
            // ارسال به صورت Object و دریافت مستقیم پاسخ
            ApiResponse response = apiClient.post(
                    "/api/advertisements",
                    request,
                    ApiResponse.class
            );
            if (response != null && response.isSuccess()) {
                // تبدیل داده‌ی دریافتی به AdvertisementResponse
                return new com.fasterxml.jackson.databind.ObjectMapper()
                        .convertValue(response.getData(), AdvertisementResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AdvertisementResponse> getActiveAds() {
        try {
            ApiResponse response = apiClient.get(
                    "/api/advertisements",
                    ApiResponse.class
            );

            if (response != null && response.isSuccess() && response.getData() != null) {
                return Arrays.asList(new com.fasterxml.jackson.databind.ObjectMapper()
                        .convertValue(response.getData(), AdvertisementResponse[].class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AdvertisementResponse> getPendingAds() {
        try {
            ApiResponse response = apiClient.get(
                    "/api/advertisements/pending",
                    ApiResponse.class
            );
            if (response != null && response.isSuccess() && response.getData() != null) {
                return Arrays.asList(new com.fasterxml.jackson.databind.ObjectMapper()
                        .convertValue(response.getData(), AdvertisementResponse[].class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean approveAd(Long adId) {
        try {
            ApiResponse response = apiClient.post(
                    "/api/admin/advertisements/" + adId + "/approve",
                    null,
                    ApiResponse.class
            );
            return response != null && response.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean rejectAd(Long adId) {
        try {
            ApiResponse response = apiClient.post(
                    "/api/admin/advertisements/" + adId + "/reject",
                    null,
                    ApiResponse.class
            );
            return response != null && response.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean uploadAdImage(Long adId, java.io.File file) {
        try {
            // آپلود عکس به اندپوینت مربوطه در بک‌اند
            // اگر از RestTemplate یا روش‌های Multipart استفاده می‌کنید یا موقتاً برای تست ساده:
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}