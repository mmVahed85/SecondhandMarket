package com.secondhand.controller;

import com.secondhand.model.Ad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class AdDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label cityLabel;
    @FXML private Label categoryLabel;
    @FXML private Label descriptionLabel;
    @FXML private ImageView adImageView;
    @FXML private Label sellerNameLabel;
    @FXML private Label sellerRatingLabel;
    @FXML private Label messageLabel;

    private Ad currentAd; // آگهی فعلی که در حال نمایش است

    // داشبورد این متد را صدا می‌زند و آگهی را به آن پاس می‌دهد
    public void setAd(Ad ad) {
        this.currentAd = ad;

        titleLabel.setText(ad.getTitle());
        priceLabel.setText("قیمت: " + ad.getPrice() + " تومان");
        cityLabel.setText("شهر: " + ad.getCity());
        categoryLabel.setText("دسته‌بندی: " + (ad.getCategory() != null ? ad.getCategory() : "نامشخص"));
        descriptionLabel.setText(ad.getDescription() != null ? ad.getDescription() : "توضیحاتی ثبت نشده است.");
        sellerNameLabel.setText("فروشنده: " + (ad.getSellerName() != null ? ad.getSellerName() : "کاربر سامانه"));
        sellerRatingLabel.setText("امتیاز فروشنده: ۴.۵ از ۵"); // فعلاً یک عدد فرضی

        // نمایش عکس اگر وجود داشت
        if (ad.getImageBase64() != null && !ad.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(ad.getImageBase64());
                adImageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
            } catch (Exception e) {
                System.err.println("خطا در بارگذاری عکس آگهی");
            }
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addToFavorites(ActionEvent event) {
        if (currentAd != null) {
            // صدا زدن متدی که در SessionManager ساختیم
            com.secondhand.util.SessionManager.addToFavorites(currentAd);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("این آگهی با موفقیت به لیست علاقه‌مندی‌های شما اضافه شد!");
        }
    }

    @FXML
    public void startChat(ActionEvent event) {
        messageLabel.setStyle("-fx-text-fill: blue;");
        messageLabel.setText("در حال انتقال به صفحه چت...");
        // بعداً کد رفتن به صفحه گفت‌وگو را اینجا می‌نویسیم
    }
}