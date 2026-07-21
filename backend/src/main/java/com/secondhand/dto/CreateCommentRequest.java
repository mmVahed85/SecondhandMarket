package com.secondhand.dto;

import javax.validation.constraints.NotBlank;

public class CreateCommentRequest {

    @NotBlank
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