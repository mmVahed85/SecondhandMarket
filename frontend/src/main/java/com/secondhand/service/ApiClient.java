package com.secondhand.service;

import com.secondhand.util.ApiConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.OutputStream;

public class ApiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public <T> T get(String endpoint, Class<T> responseType) {
        try {
            URL url = new URL(ApiConfig.BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return objectMapper.readValue(
                    response.toString(),
                    responseType
            );
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T post(String endpoint, Object body, Class<T> responseType) {
        try {
            URL url = new URL(ApiConfig.BASE_URL + endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setDoOutput(true);
            String json = objectMapper.writeValueAsString(body);
            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return objectMapper.readValue(response.toString(),responseType);
        }
        catch (Exception e) {
            throw new RuntimeException("API Error: " + e.getMessage(), e);
        }
    }
}