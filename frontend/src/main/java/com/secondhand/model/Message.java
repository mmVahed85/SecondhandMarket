package com.secondhand.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String sender;
    private String receiver;
    private String text;
    private LocalDateTime timestamp;

    public Message(String sender, String receiver, String text) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.timestamp = LocalDateTime.now(); // زمان فعلی
    }

    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getText() { return text; }

    // یک متد کمکی برای نمایش ساعت و دقیقه (مثلا 14:30)
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timestamp.format(formatter);
    }
}