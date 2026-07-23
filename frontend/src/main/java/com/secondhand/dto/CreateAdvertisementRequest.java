package com.secondhand.dto;


import com.secondhand.entity.Category;

public class CreateAdvertisementRequest {
    private String title;
    private String description;
    private Long price;
    private String city;
    private Category category;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getPrice() { return price; }
    public void setPrice(Long price) { this.price = price; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
}