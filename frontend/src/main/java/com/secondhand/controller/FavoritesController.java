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
import java.util.Base64;
import java.util.List;

public class FavoritesController {

    @FXML
    private TilePane favoritesContainer;

    @FXML
    public void initialize() {
        loadFavorites();
    }

    private void loadFavorites() {
        favoritesContainer.getChildren().clear();

        // دریافت لیست از حافظه برنامه
        List<Ad> favoriteAds = SessionManager.getFavoriteAds();

        // اگر لیست خالی بود، یک پیام مناسب نمایش بده
        if (favoriteAds == null || favoriteAds.isEmpty()) {
            Label noAdLabel = new Label("شما هنوز هیچ آگهی‌ای را به علاقه‌مندی‌ها اضافه نکرده‌اید.");
            noAdLabel.setFont(new Font("B Yekan", 18));
            favoritesContainer.getChildren().add(noAdLabel);
            return;
        }

        // ساخت کارت برای هر آگهی
        for (Ad ad : favoriteAds) {
            VBox adCard = createAdCard(ad);
            favoritesContainer.getChildren().add(adCard);
        }
    }

    private VBox createAdCard(Ad ad) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 330);

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

        // دکمه مشاهده جزئیات (مثل داشبورد)
        Button detailsButton = new Button("مشاهده جزئیات");
        detailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        detailsButton.setMaxWidth(Double.MAX_VALUE);
        detailsButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ad-details.fxml"));
                Parent root = loader.load();
                AdDetailsController detailsController = loader.getController();
                detailsController.setAd(ad);
                Scene currentScene = ((Node) e.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // دکمه جدید: حذف از علاقه‌مندی‌ها
        Button removeButton = new Button("حذف از علاقه‌مندی");
        removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        removeButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setOnAction(e -> {
            SessionManager.getFavoriteAds().remove(ad);
            loadFavorites(); // صفحه را رفرش می‌کنیم تا کارت فوراً غیب شود!
        });

        card.getChildren().addAll(imageView, titleLabel, priceLabel, detailsButton, removeButton);
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