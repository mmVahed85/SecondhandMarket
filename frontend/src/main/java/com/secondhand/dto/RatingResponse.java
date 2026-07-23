package com.secondhand.dto;

public class RatingResponse {

    private Double averageScore;

    private Integer ratingCount;

    private Integer myScore;

    public RatingResponse() {
    }

    public RatingResponse(Double averageScore,
                          Integer ratingCount,
                          Integer myScore) {

        this.averageScore = averageScore;
        this.ratingCount = ratingCount;
        this.myScore = myScore;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getMyScore() {
        return myScore;
    }

    public void setMyScore(Integer myScore) {
        this.myScore = myScore;
    }
}