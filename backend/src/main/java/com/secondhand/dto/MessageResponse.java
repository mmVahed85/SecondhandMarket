package com.secondhand.dto;

import java.time.LocalDateTime;

public class MessageResponse {

    private Long id;

    private String sender;

    private String senderFirstname;

    private String senderLastname;

    private String text;

    private boolean seen;

    private LocalDateTime createdAt;

    public MessageResponse() {
    }

    public MessageResponse(
            Long id,
            String sender,
            String senderFirstname,
            String senderLastname,
            String text,
            boolean seen,
            LocalDateTime createdAt) {

        this.id = id;
        this.sender = sender;
        this.text = text;
        this.seen = seen;
        this.createdAt = createdAt;
        this.senderFirstname = senderFirstname;
        this.senderLastname = senderLastname;
    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderFirstname() {
        return senderFirstname;
    }

    public String getSenderLastname() {
        return senderLastname;
    }

    public String getText() {
        return text;
    }

    public boolean isSeen() {
        return seen;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}