package com.secondhand.model;

public class CreateAdRequest {
    private String title;
    private String description;
    private long price;
    private String city;
    private String category;
    private String imageBase64; // فیلد جدید برای عکس

    public CreateAdRequest(String title, String description, long price, String city, String category, String imageBase64) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.city = city;
        this.category = category;
        this.imageBase64 = imageBase64;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
}