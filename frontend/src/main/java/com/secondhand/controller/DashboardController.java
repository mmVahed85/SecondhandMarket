package com.secondhand.controller;

import com.secondhand.model.Ad;
import com.secondhand.service.AdApi;
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

public class DashboardController {

    @FXML
    private TilePane adsContainer;

    private final AdApi adApi = new AdApi();

    @FXML
    public void initialize() {
        loadAdsFromServer();
    }

    // متد دریافت و نمایش آگهی‌ها
    private void loadAdsFromServer() {
        adsContainer.getChildren().clear();
        Ad[] ads = adApi.getActiveAds();

        // تغییر جدید: اگر سرور دیتایی نداشت، داده‌های تقلبی خودمان را لود کن
        if (ads == null || ads.length == 0) {
            System.out.println("دریافت از سرور خالی بود. بارگذاری داده‌های تستی...");
            ads = getMockAds();
        }

        for (Ad ad : ads) {
            VBox adCard = createAdCard(ad);
            adsContainer.getChildren().add(adCard);
        }
    }

    // متد ساخت ظاهر کارت آگهی (داینامیک)
    private VBox createAdCard(Ad ad) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 300);

        // ۱. بخش عکس آگهی
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        if (ad.getImageBase64() != null && !ad.getImageBase64().isEmpty()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(ad.getImageBase64());
                imageView.setImage(new Image(new ByteArrayInputStream(imageBytes)));
            } catch (Exception e) {
                // اگر عکس نامعتبر بود، می‌توانید یک عکس پیش‌فرض قرار دهید
            }
        }

        // ۲. عنوان آگهی
        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setFont(new Font("B Yekan", 16));
        titleLabel.setStyle("-fx-font-weight: bold;");

        // ۳. قیمت
        Label priceLabel = new Label("قیمت: " + ad.getPrice() + " تومان");
        priceLabel.setFont(new Font("B Yekan", 14));
        priceLabel.setStyle("-fx-text-fill: #4CAF50;");

        // ۴. شهر
        Label cityLabel = new Label("شهر: " + ad.getCity());
        cityLabel.setFont(new Font("B Yekan", 12));
        cityLabel.setStyle("-fx-text-fill: #757575;");

        // ۵. دکمه مشاهده جزئیات
        // ۵. دکمه مشاهده جزئیات
        Button detailsButton = new Button("مشاهده جزئیات");
        detailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        detailsButton.setOnAction(e -> {
            try {
                // الف: لود کردن فایل گرافیکی بدون اینکه فوراً نمایشش دهیم
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ad-details.fxml"));
                Parent root = loader.load();

                // ب: گرفتن کنترلر صفحه بعد و تزریق اطلاعات آگهی به آن
                AdDetailsController detailsController = loader.getController();
                detailsController.setAd(ad);

                // ج: حالا که اطلاعات ست شد، صفحه را تغییر می‌دهیم
                Scene currentScene = ((Node) e.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // اضافه کردن همه المان‌ها به کارت
        card.getChildren().addAll(imageView, titleLabel, priceLabel, cityLabel, detailsButton);
        return card;
    }

    @FXML
    public void goToCreateAd(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/create-ad.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Ad[] getMockAds() {
        Ad mock1 = new Ad(1L, "لپ‌تاپ ایسوس گیمینگ", 45000000L, "تهران", "");
        mock1.setDescription("لپ‌تاپ بسیار تمیز، مناسب برای بازی و کارهای گرافیکی. بدون خط و خش.");
        mock1.setCategory("الکترونیک");
        mock1.setSellerName("علی احمدی");

        Ad mock2 = new Ad(2L, "دوچرخه کوهستان", 8500000L, "اصفهان", "");
        mock2.setDescription("دوچرخه دنده‌ای شیمانو، ترمز دیسکی، سایز ۲۶. فقط یک ماه کار کرده است.");
        mock2.setCategory("ورزشی");
        mock2.setSellerName("سارا رضایی");

        return new Ad[]{mock1, mock2};
    }

    @FXML
    public void goToFavorites(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/favorites.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}