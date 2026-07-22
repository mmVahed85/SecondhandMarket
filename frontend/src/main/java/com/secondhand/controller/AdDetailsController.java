package com.secondhand.controller;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.model.Advertisement;
import com.secondhand.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    // المان‌های جدید امتیاز و نظر
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextField reviewCommentField;

    private AdvertisementResponse currentAd;

    @FXML
    public void initialize() {
        // پر کردن منوی کشویی با اعداد ۱ تا ۵
        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
    }

    public void setAd(AdvertisementResponse ad) {
        this.currentAd = ad;

        titleLabel.setText(ad.getTitle());
        priceLabel.setText("قیمت: " + ad.getPrice() + " تومان");
        cityLabel.setText("شهر: " + ad.getCity());
        categoryLabel.setText("دسته‌بندی: " + (ad.getCategory() != null ? ad.getCategory() : "نامشخص"));
        descriptionLabel.setText(ad.getDescription() != null ? ad.getDescription() : "توضیحاتی ثبت نشده است.");
        sellerNameLabel.setText("فروشنده: " + (ad.getOwnerUsername() != null ? ad.getOwnerUsername() : "کاربر سامانه"));
        sellerRatingLabel.setText("میانگین امتیاز: ۴.۵ از ۵");

        if (ad.getImages() != null && !ad.getImages().isEmpty()) {

            try {

                adImageView.setImage(
                        new Image(
                                ad.getImages().get(0).getUrl(),
                                true
                        )
                );

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
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("این آگهی با موفقیت به لیست علاقه‌مندی‌های شما اضافه شد!");
        }
    }

    @FXML
    public void submitRating(ActionEvent event) {
        Integer selectedRating = ratingComboBox.getValue();
        String comment = reviewCommentField.getText();

        if (selectedRating == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("خطا: لطفاً ابتدا یک امتیاز (بین ۱ تا ۵) انتخاب کنید.");
            return;
        }

        // چاپ در ترمینال برای تست
        System.out.println("امتیاز " + selectedRating + " برای فروشنده '" + currentAd.getOwnerUsername() + "' ثبت شد.");
        if (comment != null && !comment.trim().isEmpty()) {
            System.out.println("نظر ثبت شده کاربر: " + comment);
        }

        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("سپاس از شما! امتیاز و نظر شما با موفقیت ثبت شد.");

        // غیرفعال کردن فیلدها و دکمه پس از ثبت
        ratingComboBox.setDisable(true);
        reviewCommentField.setDisable(true);
        ((Button) event.getSource()).setDisable(true);
    }

    @FXML
    public void startChat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat.fxml"));
            Parent root = loader.load();

            // گرفتن کنترلر چت و پاس دادن اسم فروشنده و عنوان آگهی به آن
            ChatController chatController = loader.getController();
            String seller = currentAd.getOwnerUsername() != null ? currentAd.getOwnerUsername() : "فروشنده";
            chatController.initData(seller, currentAd.getTitle());

            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            System.err.println("خطا در باز کردن صفحه چت:");
            e.printStackTrace();
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("خطا در باز کردن صفحه گفت‌وگو!");
        }
    }
}