package com.secondhand.entity;

import java.util.List;

public class Advertisement{

 public Long id;
 public String title,description,city,ownerUsername;
 public Long price;
 public Category category;
 public AdvertisementStatus status;
 public Double averageRating;
 public List<String> images;

public Advertisement() {
   
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

 public Long getPrice() {
    return price;
 }

 public void setPrice(Long price) {
    this.price = price;
 }

 public Category getCategory() {
    return category;
 }

 public void setCategory(Category category) {
    this.category = category;
 }

 public AdvertisementStatus getStatus() {
    return status;
 }

 public void setStatus(AdvertisementStatus status) {
    this.status = status;
 }

 public Double getAverageRating() {
    return averageRating;
 }

 public void setAverageRating(Double averageRating) {
    this.averageRating = averageRating;
 }

 public List<String> getImages() {
    return images;
 }

 public void setImages(List<String> images) {
    this.images = images;
 }
}