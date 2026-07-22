package com.secondhand.service;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.secondhand.dto.ChatRoomResponse;
import com.secondhand.dto.MessageResponse;
import com.secondhand.dto.SendMessageRequest;
import com.secondhand.util.ApiResponse;

public class ChatApi {

    private final ApiClient apiClient = new ApiClient();

    public ApiResponse<List<ChatRoomResponse>> getChats() {

        return apiClient.get(
                "/api/chats",
                new TypeReference<ApiResponse<List<ChatRoomResponse>>>() {}
        );
    }

    public ApiResponse<List<MessageResponse>> getMessages(Long chatId) {

        return apiClient.get(
                "/api/chats/" + chatId,
                new TypeReference<ApiResponse<List<MessageResponse>>>() {}
        );
    }

    public ApiResponse<MessageResponse> sendMessage(
            Long chatId,
            SendMessageRequest request) {

        return apiClient.post(
                "/api/chats/" + chatId + "/messages",
                request,
                new TypeReference<ApiResponse<MessageResponse>>() {}
        );
    }

    public ApiResponse<ChatRoomResponse> createOrGetRoom(Long advertisementId) {

        return apiClient.post(
                "/api/chats/" + advertisementId + "/room",
                null,
                new TypeReference<ApiResponse<ChatRoomResponse>>() {}
        );

    }

}
