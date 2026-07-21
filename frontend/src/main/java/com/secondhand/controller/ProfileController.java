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

        if (myAds.isEmpty()) {
            myAds.add(new Ad(10L, "مبل راحتی ۷ نفره", 12000000L, "تهران", null));
            myAds.add(new Ad(11L, "میز تحریر چوبی", 1500000L, "تهران", null));

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
        card.setSpacing(8);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 360); // ارتفاع را کمی بیشتر کردیم تا دکمه‌ها جا بشوند

        ImageView imageView = new ImageView();
        imageView.setFitWidth(180);
        imageView.setFitHeight(130);
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
        priceLabel.setStyle("-fx-text-fill: #2c3e50;");

        // وضعیت فعلی آگهی
        Label statusLabel = new Label("وضعیت: فعال");
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
                System.err.println("خطا در باز کردن صفحه ویرایش:");
                ex.printStackTrace();
            }
        });

        Button soldButton = new Button("✔ تغییر به فروخته شده");
        soldButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        soldButton.setMaxWidth(Double.MAX_VALUE);
        soldButton.setOnAction(e -> {
            statusLabel.setText("وضعیت: فروخته شده");
            statusLabel.setStyle("-fx-text-fill: #7f8c8d;"); // رنگ خاکستری
            // غیرفعال کردن دکمه پس از فروش
            ((Button) e.getSource()).setDisable(true);
            System.out.println("وضعیت آگهی به فروخته شده تغییر کرد.");
        });

        Button deleteButton = new Button("🗑 حذف آگهی");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setOnAction(e -> {
            myAds.remove(ad);
            loadMyAds();
            System.out.println("آگهی با موفقیت حذف شد.");
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