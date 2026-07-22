package com.secondhand.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhand.util.ApiConfig;
import com.secondhand.util.ApiResponse;
import com.secondhand.util.SessionManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

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

    // ---------------------- PUT ----------------------

    public <T> ApiResponse<T> put(
            String endpoint,
            Object body,
            TypeReference<ApiResponse<T>> typeReference) {

        try {

            HttpURLConnection connection =
                    createConnection(endpoint, "PUT");

            connection.setDoOutput(true);

            String json = objectMapper.writeValueAsString(body);

            try (OutputStream os = connection.getOutputStream()) {

                os.write(json.getBytes("UTF-8"));
                os.flush();

            }

            String response = readResponse(connection);

            return objectMapper.readValue(response, typeReference);

        } catch (Exception e) {

            throw new RuntimeException(e);

        }
    }

    // ---------------------- PUT ----------------------

    public <T> ApiResponse<T> put(
            String endpoint,
            TypeReference<ApiResponse<T>> typeReference) {

        try {

            HttpURLConnection connection =
                    createConnection(endpoint, "PUT");

            String response = readResponse(connection);

            return objectMapper.readValue(response, typeReference);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------------- DELETE ----------------------

    public <T> ApiResponse<T> delete(
            String endpoint,
            TypeReference<ApiResponse<T>> typeReference) {

        try {

            HttpURLConnection connection = createConnection(endpoint, "DELETE");

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

        if (is == null) {
            throw new RuntimeException(
                "HTTP " + connection.getResponseCode() +
                " received but response stream is null."
            );
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

    public <T> ApiResponse<T> multipart(
            String endpoint,
            String fieldName,
            File file,
            TypeReference<ApiResponse<T>> typeReference) {

        try {

            String boundary = "----Boundary" + UUID.randomUUID();

            URL url = new URL(ApiConfig.BASE_URL + endpoint);

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setDoOutput(true);

            connection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            connection.setRequestProperty(
                    "Accept",
                    "application/json");

            if (SessionManager.isLoggedIn()) {

                connection.setRequestProperty(
                        "Authorization",
                        "Bearer " + SessionManager.getToken());

            }

            try (DataOutputStream out =
                        new DataOutputStream(connection.getOutputStream())) {

                out.writeBytes("--" + boundary + "\r\n");

                out.writeBytes(
                        "Content-Disposition: form-data; name=\"" +
                                fieldName +
                                "\"; filename=\"" +
                                file.getName() +
                                "\"\r\n");

                out.writeBytes(
                        "Content-Type: application/octet-stream\r\n\r\n");

                try (FileInputStream fis =
                            new FileInputStream(file)) {

                    byte[] buffer = new byte[4096];

                    int bytesRead;

                    while ((bytesRead = fis.read(buffer)) != -1) {

                        out.write(buffer, 0, bytesRead);

                    }

                }

                out.writeBytes("\r\n");

                out.writeBytes("--" + boundary + "--\r\n");

            }

            String response = readResponse(connection);

            return objectMapper.readValue(response, typeReference);

        }

        catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

}