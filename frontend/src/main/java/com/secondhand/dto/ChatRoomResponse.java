package com.secondhand.dto;

import java.time.LocalDateTime;

public class ChatRoomResponse {

    private Long id;

    private Long advertisementId;

    private String advertisementTitle;

    private String otherUser;
    private String otherUserFirstname;
    private String otherUserLastname;

    private String lastMessage;

    private LocalDateTime lastMessageTime;

    private boolean hasUnreadMessages;

    public ChatRoomResponse() {
    }

    public ChatRoomResponse(
        Long id,
        Long advertisementId,
        String advertisementTitle,
        String otherUser,
        String otherUserFirstname, String otherUserLastname,
        String lastMessage,
        LocalDateTime lastMessageTime) {

    this.id = id;
    this.advertisementId = advertisementId;
    this.advertisementTitle = advertisementTitle;
    this.otherUser = otherUser;
    this.lastMessage = lastMessage;
    this.lastMessageTime = lastMessageTime;
    this.otherUserFirstname = otherUserFirstname;
    this.otherUserLastname = otherUserLastname;
}

    public Long getId() {
        return id;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public String getAdvertisementTitle() {
        return advertisementTitle;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isHasUnreadMessages() {
        return hasUnreadMessages;
    }

    public void setHasUnreadMessages(boolean hasUnreadMessages) {
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public String getOtherUserFirstname() {
        return otherUserFirstname;
    }

    public void setOtherUserFirstname(String otherUserFirstname) {
        this.otherUserFirstname = otherUserFirstname;
    }

    public String getOtherUserLastname() {
        return otherUserLastname;
    }

    public void setOtherUserLastname(String otherUserLastname) {
        this.otherUserLastname = otherUserLastname;
    }
}