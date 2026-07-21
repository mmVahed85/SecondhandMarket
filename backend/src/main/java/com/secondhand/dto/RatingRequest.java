package com.secondhand.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RatingRequest {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    public RatingRequest() {
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}