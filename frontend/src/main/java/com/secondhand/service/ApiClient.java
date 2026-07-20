package com.secondhand.service;

import com.secondhand.util.ApiConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.core.type.TypeReference;

public class ApiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T get(String endpoint, TypeReference<T> typeReference) {
        try {
            URL url = new URL(ApiConfig.BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            // افزودن توکن به هدر درخواست در صورت وجود
            if (com.secondhand.util.SessionManager.isLoggedIn()) {
                connection.setRequestProperty("Authorization", "Bearer " + com.secondhand.util.SessionManager.getToken());
            }

            int status = connection.getResponseCode();

            InputStream is = (status >= 200 && status < 300) ? connection.getInputStream() : connection.getErrorStream();
            StringBuilder response = new StringBuilder();

            // بررسی null نبودن استریم قبل از خواندن
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                response.append("بدون پاسخ (No Body)");
            }

            if (status >= 400) {
                System.err.println("خطای سرور در متد GET (کد " + status + "): " + response.toString());
                throw new RuntimeException("Server Error: " + status);
            }

            return objectMapper.readValue(response.toString(), typeReference);

        } catch (Exception e) {
            System.err.println("شکست در ارسال درخواست GET به: " + endpoint);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public <T> T post(String endpoint, Object body, TypeReference<T> typeReference) {
        try {
            URL url = new URL(ApiConfig.BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            // افزودن توکن به هدر درخواست در صورت وجود
            if (com.secondhand.util.SessionManager.isLoggedIn()) {
                connection.setRequestProperty("Authorization", "Bearer " + com.secondhand.util.SessionManager.getToken());
            }
            connection.setDoOutput(true);

            String json = objectMapper.writeValueAsString(body);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes("UTF-8"));
                os.flush();
            }

            int status = connection.getResponseCode();

            InputStream is = (status >= 200 && status < 300) ? connection.getInputStream() : connection.getErrorStream();
            StringBuilder response = new StringBuilder();

            // بررسی null نبودن استریم قبل از خواندن
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                response.append("بدون پاسخ (No Body)");
            }

            if (status >= 400) {
                System.err.println("خطای سرور در متد POST (کد " + status + "): " + response.toString());
                throw new RuntimeException("Server Error: " + status);
            }

            return objectMapper.readValue(response.toString(), typeReference);

        } catch (Exception e) {
            System.err.println("شکست در ارسال درخواست POST به: " + endpoint);
            e.printStackTrace();
            throw new RuntimeException("API Error: " + e.getMessage(), e);
        }
    }
}