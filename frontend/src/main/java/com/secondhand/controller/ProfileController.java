package com.secondhand.controller;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.UpdateAdvertisementRequest;
import com.secondhand.model.*;
import com.secondhand.service.AdApi;
import com.secondhand.util.ApiResponse;
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

    private final AdApi adApi = new AdApi();

    @FXML private Label usernameLabel;
    @FXML private TilePane myAdsContainer;
    private String currentUser;

    @FXML
    public void initialize() {
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
        
        ApiResponse<List<AdvertisementResponse>> response = adApi.getMyAds();

        if(response.isSuccess()) {
            if (response.getData().isEmpty()) {
                Label noAdLabel = new Label("شما هنوز هیچ آگهی‌ای ثبت نکرده‌اید.");
                noAdLabel.setFont(new Font("B Yekan", 16));
                myAdsContainer.getChildren().add(noAdLabel);
                return;
            }

            for (AdvertisementResponse ad : response.getData()) {
                VBox adCard = createMyAdCard(ad);
                myAdsContainer.getChildren().add(adCard);
            }
        }
        else {
            Label noAdLabel = new Label(response.getMessage());
            noAdLabel.setFont(new Font("B Yekan", 16));
            myAdsContainer.getChildren().add(noAdLabel);
            return;
        }
        
    }

    private VBox createMyAdCard(AdvertisementResponse ad) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 360); // ارتفاع را کمی بیشتر کردیم تا دکمه‌ها جا بشوند

        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(130);
        imageView.setPreserveRatio(true);

        if (ad.getImages() != null && !ad.getImages().isEmpty()) {

            try {

                String imageUrl = ad.getImages().get(0).getUrl();

                imageView.setImage(new Image(imageUrl, true));

            } catch (Exception e) {

                System.err.println("خطا در بارگذاری تصویر");
                e.printStackTrace();

            }

        }

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setFont(new Font("B Yekan", 16));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label priceLabel = new Label("قیمت: " + ad.getPrice() + " تومان");
        priceLabel.setFont(new Font("B Yekan", 14));
        priceLabel.setStyle("-fx-text-fill: #2c3e50;");

        // وضعیت فعلی آگهی
        Label statusLabel = new Label("وضعیت: " + ad.getStatus());
        statusLabel.setFont(new Font("B Yekan", 12));
        statusLabel.setStyle("-fx-text-fill: #4CAF50;"); // رنگ سبز برای فعال

        // --- دکمه‌های جدید ---

        Button editButton = new Button("✏ ویرایش آگهی");
        editButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        editButton.setMaxWidth(Double.MAX_VALUE);
        editButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit-ad.fxml"));
                Parent root = loader.load();
                
                // ارسال اطلاعات آگهی انتخاب شده به صفحه ویرایش
                EditAdController editController = loader.getController();
                editController.initData(ad);

                Scene currentScene = ((Node) e.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (Exception ex) {
                ex.printStackTrace();

                Throwable cause = ex;
                while (cause.getCause() != null) {
                    cause = cause.getCause();
                    System.out.println("CAUSE = " + cause);
                }
            }
        });

        Button soldButton = new Button("✔ تغییر به فروخته شده");
        soldButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        soldButton.setMaxWidth(Double.MAX_VALUE);
        if(ad.getStatus().equals(AdvertisementStatus.ACTIVE)) {
            soldButton.setOnAction(e -> {
                UpdateAdvertisementRequest request = new UpdateAdvertisementRequest();
                request.setStatus(AdvertisementStatus.SOLD);
                ApiResponse<AdvertisementResponse> response = adApi.updateAd(ad.getId(), request);
                if(response.isSuccess()) {
                    statusLabel.setStyle("-fx-text-fill: #7f8c8d;"); // رنگ خاکستری
                    ((Button) e.getSource()).setDisable(true);
                    loadMyAds();
                }
                else {
                    System.err.println(response.getMessage());
                }
            });
        }
        else {
            soldButton.setDisable(true);
        }

        Button deleteButton = new Button("🗑 حذف آگهی");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setOnAction(e -> {
            ApiResponse<AdvertisementResponse> response = adApi.delete(ad.getId());
            if(response.isSuccess()) {
                loadMyAds();
            }
            else {
                System.out.println(response.getMessage());
            }
        });

        card.getChildren().addAll(imageView, titleLabel, priceLabel, statusLabel, editButton, soldButton, deleteButton);
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