package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.util.ApiConfig;
import com.secondhand.util.SessionManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    // نادیده گرفتن فیلدهای ناشناخته برای جلوگیری از ارور UnrecognizedPropertyException
    // تعریف یک‌باره‌ی ObjectMapper با تنظیمات کامل (هم نادیده گرفتن فیلد ناشناخته و هم پشتیبانی از تاریخ)
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    public ApiClient() {
        // سازنده خالی می‌ماند چون مقداردهی اولیه در بالا به صورت کامل انجام شده است
    }
    // ==========================================
    // متدهای GET
    // ==========================================
    public <T> T get(String endpoint, Class<T> responseType) {
        return executeRequest("GET", endpoint, null, responseType, null);
    }

    public <T> T get(String endpoint, TypeReference<T> typeReference) {
        return executeRequest("GET", endpoint, null, null, typeReference);
    }

    // ==========================================
    // متدهای POST
    // ==========================================
    public <T> T post(String endpoint, Object body, Class<T> responseType) {
        return executeRequest("POST", endpoint, body, responseType, null);
    }

    public <T> T post(String endpoint, Object body, TypeReference<T> typeReference) {
        return executeRequest("POST", endpoint, body, null, typeReference);
    }

    // ==========================================
    // هسته اصلی ارسال درخواست‌ها
    // ==========================================
    private <T> T executeRequest(String method, String endpoint, Object body, Class<T> responseType, TypeReference<T> typeReference) {
        try {
            URL url = new URL(ApiConfig.BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Accept", "application/json");

            if (SessionManager.isLoggedIn() && SessionManager.getToken() != null) {
                connection.setRequestProperty("Authorization", "Bearer " + SessionManager.getToken());
            }

            if (body != null) {
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);
                String json = objectMapper.writeValueAsString(body);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(json.getBytes("UTF-8"));
                    os.flush();
                }
            }

            int status = connection.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? connection.getInputStream() : connection.getErrorStream();
            StringBuilder response = new StringBuilder();

            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
            }

            if (status >= 400) {
                System.err.println("خطای سرور (" + status + "): " + response.toString());
                // در صورت خطا، اگر سرور ApiResponse فرستاده باشد، آن را پارس می‌کنیم
                if (typeReference != null) {
                    return objectMapper.readValue(response.toString(), typeReference);
                } else if (responseType != null && responseType != String.class) {
                    return objectMapper.readValue(response.toString(), responseType);
                }
                throw new RuntimeException("Server Error: " + status + " - " + response.toString());
            }

            if (responseType == String.class) {
                return (T) response.toString();
            }

            if (typeReference != null) {
                return objectMapper.readValue(response.toString(), typeReference);
            } else {
                return objectMapper.readValue(response.toString(), responseType);
            }

        } catch (Exception e) {
            System.err.println("شکست در ارسال درخواست " + method + " به: " + endpoint);
            e.printStackTrace();
            throw new RuntimeException("API Error: " + e.getMessage(), e);
        }
    }
}