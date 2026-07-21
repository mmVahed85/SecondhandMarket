package com.secondhand.controller;

import com.secondhand.dto.ChatRoomResponse;
import com.secondhand.dto.MessageResponse;
import com.secondhand.dto.SendMessageRequest;
import com.secondhand.service.ChatService;
import com.secondhand.util.ApiResponse;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/chats")
@Validated
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ApiResponse<List<ChatRoomResponse>> getChats(
            Authentication authentication) {

        return chatService.getChats(
                authentication.getName()
        );
    }

    @GetMapping("/{chatId}")
    public ApiResponse<List<MessageResponse>> getMessages(
            @PathVariable Long chatId,
            Authentication authentication) {

        return chatService.getMessages(
                chatId,
                authentication.getName()
        );
    }

    @PostMapping("/{advertisementId}/chat")
    public ApiResponse<MessageResponse> sendMessage(

            @PathVariable Long advertisementId,

            @Valid @RequestBody SendMessageRequest request,

            Authentication authentication) {

        return chatService.sendMessage(
                advertisementId,
                request,
                authentication.getName()
        );

    }

}