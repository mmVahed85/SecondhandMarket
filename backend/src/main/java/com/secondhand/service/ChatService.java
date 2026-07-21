package com.secondhand.service;

import com.secondhand.dto.*;
import com.secondhand.entity.*;
import com.secondhand.repository.*;
import com.secondhand.util.ApiResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;

    public ChatService(
            ChatRoomRepository chatRoomRepository,
            MessageRepository messageRepository,
            AdvertisementRepository advertisementRepository,
            UserService userService) {

        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
    }

    public ApiResponse<MessageResponse> sendMessage(Long advertisementId, SendMessageRequest request, String username) {

        Advertisement advertisement =
                advertisementRepository.findById(advertisementId)
                        .orElse(null);

        if (advertisement == null) {
            return new ApiResponse<>(false,
                    "Advertisement not found",
                    null);
        }

        if (advertisement.getStatus() != AdvertisementStatus.ACTIVE) {

            return new ApiResponse<>(
                    false,
                    "Advertisement is not active",
                    null
            );

        }

        User sender = userService.findByUsername(username);

        if (sender == null) {
            return new ApiResponse<>(false,
                    "User not found",
                    null);
        }

        User seller = advertisement.getOwner();

        if (seller.getId().equals(sender.getId())) {
            return new ApiResponse<>(false,
                    "You cannot chat with yourself",
                    null);
        }

        ChatRoom chatRoom =
                chatRoomRepository
                        .findByAdvertisementAndBuyerAndSeller(
                                advertisement,
                                sender,
                                seller
                        )
                        .orElse(null);

        if (chatRoom == null) {

            chatRoom = new ChatRoom();

            chatRoom.setAdvertisement(advertisement);

            chatRoom.setBuyer(sender);

            chatRoom.setSeller(seller);

            chatRoomRepository.save(chatRoom);

        }

        Message message = new Message();

        message.setChatRoom(chatRoom);

        message.setSender(sender);

        message.setText(request.getText());

        messageRepository.save(message);

        MessageResponse response =
                new MessageResponse(
                        message.getId(),
                        sender.getUsername(),
                        message.getText(),
                        message.isSeen(),
                        message.getCreatedAt()
                );

        return new ApiResponse<>(
                true,
                "Message sent successfully",
                response
        );

    }

    public ApiResponse<List<MessageResponse>> getMessages(Long chatRoomId, String username) {

        ChatRoom chatRoom =
                chatRoomRepository.findById(chatRoomId)
                        .orElse(null);

        if (chatRoom == null) {
            return new ApiResponse<>(false,
                    "Chat not found",
                    null);
        }

        if (!chatRoom.getBuyer().getUsername().equals(username)
                &&
            !chatRoom.getSeller().getUsername().equals(username)) {

            return new ApiResponse<>(false,
                    "Access denied",
                    null);

        }

        List<Message> messages =
                messageRepository
                        .findByChatRoomOrderByCreatedAtAsc(chatRoom);

        for (Message message : messages) {

            if (!message.getSender().getUsername().equals(username)
                    && !message.isSeen()) {

                message.setSeen(true);

                messageRepository.save(message);

            }

        }

        List<MessageResponse> response =
                new ArrayList<>();

        for (Message message : messages) {

            response.add(

                    new MessageResponse(

                            message.getId(),

                            message.getSender().getUsername(),

                            message.getText(),

                            message.isSeen(),

                            message.getCreatedAt()

                    )

            );

        }

        return new ApiResponse<>(
                true,
                "Messages loaded successfully",
                response
        );

    }

    public ApiResponse<List<ChatRoomResponse>> getChats(String username) {

        User user = userService.findByUsername(username);

        if (user == null) {
            return new ApiResponse<>(false,
                    "User not found",
                    null);
        }

        List<ChatRoom> rooms =
                chatRoomRepository.findByBuyerOrSeller(user, user);

        List<ChatRoomResponse> response = new ArrayList<>();

        for (ChatRoom room : rooms) {

            String otherUser;

            if (room.getBuyer().getId().equals(user.getId())) {
                otherUser = room.getSeller().getUsername();
            } else {
                otherUser = room.getBuyer().getUsername();
            }

            List<Message> messages =
                    messageRepository.findByChatRoomOrderByCreatedAtAsc(room);

            String lastMessage = "";

            LocalDateTime lastMessageTime = null;

            boolean hasUnreadMessages = false;

            if (!messages.isEmpty()) {

                Message last = messages.get(messages.size() - 1);

                lastMessage = last.getText();

                lastMessageTime = last.getCreatedAt();

                for (Message message : messages) {

                    if (!message.getSender().getUsername().equals(username)
                            && !message.isSeen()) {

                        hasUnreadMessages = true;
                        break;

                    }

                }

            }

            ChatRoomResponse dto =
                    new ChatRoomResponse(
                            room.getId(),
                            room.getAdvertisement().getId(),
                            room.getAdvertisement().getTitle(),
                            otherUser,
                            lastMessage,
                            lastMessageTime
                    );

            dto.setHasUnreadMessages(hasUnreadMessages);

            response.add(dto);

        }

        return new ApiResponse<>(
                true,
                "Chats loaded successfully",
                response
        );

    }
}