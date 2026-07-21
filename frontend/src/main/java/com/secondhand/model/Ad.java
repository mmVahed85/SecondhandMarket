package com.secondhand.model;

public class Ad {
    private Long id;
    private String title;
    private long price;
    private String city;
    private String imageBase64;

    // فیلدهای جدید برای صفحه جزئیات
    private String description;
    private String category;
    private String sellerName;

    // سازنده خالی (Constructor)
    public Ad() {}

    public Ad(Long id, String title, long price, String city, String imageBase64) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.city = city;
        this.imageBase64 = imageBase64;
    }

    // Getter ها و Setter های قدیمی
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public long getPrice() { return price; }
    public void setPrice(long price) { this.price = price; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }

    // Getter ها و Setter های جدید (بسیار مهم)
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
}