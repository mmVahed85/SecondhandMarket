package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.util.ApiConfig;
import com.secondhand.util.ApiResponse;
import com.secondhand.util.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ---------------------- GET ----------------------

    public <T> ApiResponse<T> get(
            String endpoint,
            TypeReference<ApiResponse<T>> typeReference) {

        try {

            HttpURLConnection connection = createConnection(endpoint, "GET");

            String response = readResponse(connection);

            return objectMapper.readValue(response, typeReference);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    // ---------------------- POST ----------------------

    public <T> ApiResponse<T> post(
            String endpoint,
            Object body,
            TypeReference<ApiResponse<T>> typeReference) {

        try {

            HttpURLConnection connection = createConnection(endpoint, "POST");

            connection.setDoOutput(true);

            String json = objectMapper.writeValueAsString(body);

            try (OutputStream os = connection.getOutputStream()) {

                os.write(json.getBytes("UTF-8"));

            }

            String response = readResponse(connection);

            return objectMapper.readValue(response, typeReference);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    // ---------------------- PRIVATE ----------------------

    private HttpURLConnection createConnection(
            String endpoint,
            String method) throws Exception {

        URL url = new URL(ApiConfig.BASE_URL + endpoint);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method);

        connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8");

        connection.setRequestProperty(
                "Accept",
                "application/json");

        if (SessionManager.isLoggedIn()) {

            connection.setRequestProperty(
                    "Authorization",
                    "Bearer " + SessionManager.getToken());

        }

        return connection;

    }

    private String readResponse(
            HttpURLConnection connection) throws Exception {

        InputStream is;

        if (connection.getResponseCode() >= 400) {

            is = connection.getErrorStream();

        } else {

            is = connection.getInputStream();

        }

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(is));

        StringBuilder builder =
                new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {

            builder.append(line);

        }

        reader.close();

        return builder.toString();

    }

}