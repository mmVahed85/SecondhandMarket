package com.secondhand.dto;

public class CreateCommentRequest {

    private String text;

    public CreateCommentRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}