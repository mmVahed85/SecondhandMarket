package com.secondhand.controller;

import com.secondhand.model.Ad;
import com.secondhand.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private TilePane myAdsContainer;

    private List<Ad> myAds = new ArrayList<>();
    private String currentUser;

    @FXML
    public void initialize() {
        // دریافت نام کاربری لاگین شده
        currentUser = SessionManager.getLoggedInUsername();
        if (currentUser != null) {
            usernameLabel.setText(currentUser);
        } else {
            usernameLabel.setText("کاربر مهمان");
            currentUser = "نامشخص";
        }

        loadMyAds();
    }

    private void loadMyAds() {
        myAdsContainer.getChildren().clear();

        // در اینجا باید از سرور فقط آگهی‌های این کاربر را بگیریم
        // فعلاً به صورت تستی چند آگهی می‌سازیم که نام فروشنده‌شان نام خودمان باشد
        if (myAds.isEmpty()) {
            myAds.add(new Ad(10L, "مبل راحتی ۷ نفره", 12000000L, "تهران", null));
            myAds.add(new Ad(11L, "میز تحریر چوبی", 1500000L, "تهران", null));

            // ست کردن نام ما به عنوان فروشنده
            for (Ad ad : myAds) {
                ad.setSellerName(currentUser);
            }
        }

        if (myAds.isEmpty()) {
            Label noAdLabel = new Label("شما هنوز هیچ آگهی‌ای ثبت نکرده‌اید.");
            noAdLabel.setFont(new Font("B Yekan", 16));
            myAdsContainer.getChildren().add(noAdLabel);
            return;
        }

        for (Ad ad : myAds) {
            VBox adCard = createMyAdCard(ad);
            myAdsContainer.getChildren().add(adCard);
        }
    }

    private VBox createMyAdCard(Ad ad) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 300);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        if (ad.getImageBase64() != null && !ad.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(ad.getImageBase64());
                imageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
            } catch (Exception e) {}
        }

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setFont(new Font("B Yekan", 16));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label priceLabel = new Label("قیمت: " + ad.getPrice() + " تومان");
        priceLabel.setFont(new Font("B Yekan", 14));
        priceLabel.setStyle("-fx-text-fill: #4CAF50;");

        // دکمه حذف آگهی (چون این آگهی مال خودمان است)
        Button deleteButton = new Button("حذف آگهی");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setOnAction(e -> {
            // حذف از لیست
            myAds.remove(ad);
            // رفرش کردن صفحه
            loadMyAds();
            System.out.println("درخواست حذف آگهی " + ad.getTitle() + " به سرور ارسال شد.");
        });

        card.getChildren().addAll(imageView, titleLabel, priceLabel, deleteButton);
        return card;
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
}