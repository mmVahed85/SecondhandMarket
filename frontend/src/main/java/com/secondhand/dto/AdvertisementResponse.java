package com.secondhand.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.secondhand.model.AdvertisementStatus;
import com.secondhand.model.Category;

public class AdvertisementResponse {

    private Long id;
    private String title;
    private String description;
    private Long price;
    private String city;
    private String ownerUsername;
    private AdvertisementStatus status;
    private Long viewCount;
    private LocalDateTime createdAt;
    private Category category;
    private List<ImageResponse> images = new ArrayList<>();
    private Double averageRating;
    private Integer ratingCount;

    public AdvertisementResponse() {
    }

    public AdvertisementResponse(Long id, String title, String description, Long price, String city, String ownerUsername, AdvertisementStatus status, Long viewCount, LocalDateTime createdAt, Category category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.city = city;
        this.ownerUsername = ownerUsername;
        this.status = status;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public AdvertisementStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertisementStatus status) {
        this.status = status;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ImageResponse> images) {
        this.images = images;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }
}