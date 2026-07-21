package com.secondhand.dto;

public class ChatRoomResponse {

    private Long id;

    private Long advertisementId;

    private String advertisementTitle;

    private String otherUser;

    public ChatRoomResponse() {
    }

    public ChatRoomResponse(
            Long id,
            Long advertisementId,
            String advertisementTitle,
            String otherUser) {

        this.id = id;
        this.advertisementId = advertisementId;
        this.advertisementTitle = advertisementTitle;
        this.otherUser = otherUser;
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
}