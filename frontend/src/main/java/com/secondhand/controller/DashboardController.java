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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private TilePane adsContainer;

    // المان‌های فیلتر
    @FXML private TextField searchField;
    @FXML private TextField cityFilterField;
    @FXML private TextField categoryFilterField;

    private final AdApi adApi = new AdApi();

    // یک لیست برای نگهداری تمام آگهی‌های دریافتی از سرور (برای سرعت در فیلتر کردن)
    private List<Ad> allAds = new ArrayList<>();

    @FXML
    public void initialize() {
        loadAdsFromServer();
    }

    private void loadAdsFromServer() {
        List<com.secondhand.dto.AdvertisementResponse> serverAds = adApi.getActiveAds();

        List<Ad> adsList = new ArrayList<>();

        if (serverAds == null || serverAds.isEmpty()) {
            System.out.println("هیچ آگهی فعالی از سرور دریافت نشد یا سرور خالی است.");
        } else {
            // تبدیل AdvertisementResponse دریافتی از بک‌اند به مدل Ad فرانت‌اند
            for (com.secondhand.dto.AdvertisementResponse resp : serverAds) {
                Ad ad = new Ad();
                ad.setId(resp.getId());
                ad.setTitle(resp.getTitle());
                ad.setPrice(resp.getPrice());
                ad.setCity(resp.getCity());
                ad.setDescription(resp.getDescription());
                ad.setCategory(resp.getCategory() != null ? resp.getCategory().name() : "");
                ad.setSellerName(resp.getOwnerUsername());

                // اگر عکسی برای آگهی ثبت شده باشد، اولین تصویر را قرار می‌دهیم
                if (resp.getImages() != null && !resp.getImages().isEmpty()) {
                    // در بک‌اند url کامل یا نسبی ذخیره می‌شود؛ اگر Base64 باشد یا لینک آپلود
                    // اینجا فیلد عکس را ست می‌کنیم
                }

                adsList.add(ad);
            }
        }

        // اگر لیستی از سرور نیامد یا خالی بود، برای تست می‌توانید از موک استفاده کنید یا لیست خالی نشان دهید
        if (adsList.isEmpty()) {
            System.out.println("بارگذاری داده‌های تستی به دلیل خالی بودن سرور...");
            adsList = Arrays.asList(getMockAds());
        }

        allAds = adsList;
        displayAds(allAds);
    }

    // متد جدید برای نمایش یک لیست مشخص از آگهی‌ها
    private void displayAds(List<Ad> adsToDisplay) {
        adsContainer.getChildren().clear();

        if (adsToDisplay == null || adsToDisplay.isEmpty()) {
            Label noAdLabel = new Label("هیچ آگهی‌ای با این مشخصات یافت نشد.");
            noAdLabel.setFont(new Font("B Yekan", 18));
            adsContainer.getChildren().add(noAdLabel);
            return;
        }

        for (Ad ad : adsToDisplay) {
            VBox adCard = createAdCard(ad);
            adsContainer.getChildren().add(adCard);
        }
    }

    // متد اعمال فیلترها (هنگام کلیک روی دکمه جستجو)
    @FXML
    public void handleFilter(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        String cityText = cityFilterField.getText().trim().toLowerCase();
        String categoryText = categoryFilterField.getText().trim().toLowerCase();

        // فیلتر کردن لیست اصلی با استفاده از Stream های جاوا
        List<Ad> filteredAds = allAds.stream().filter(ad -> {
            boolean matchesSearch = searchText.isEmpty() || ad.getTitle().toLowerCase().contains(searchText);
            boolean matchesCity = cityText.isEmpty() || (ad.getCity() != null && ad.getCity().toLowerCase().contains(cityText));
            boolean matchesCategory = categoryText.isEmpty() || (ad.getCategory() != null && ad.getCategory().toLowerCase().contains(categoryText));

            return matchesSearch && matchesCity && matchesCategory;
        }).collect(Collectors.toList());

        // نمایش لیست فیلتر شده
        displayAds(filteredAds);
    }

    // متد پاک کردن فیلترها
    @FXML
    public void clearFilters(ActionEvent event) {
        searchField.clear();
        cityFilterField.clear();
        categoryFilterField.clear();

        // نمایش دوباره تمام آگهی‌ها
        displayAds(allAds);
    }

    // متد ساخت ظاهر کارت آگهی (بدون تغییر نسبت به قبل)
    private VBox createAdCard(Ad ad) {
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

        Label cityLabel = new Label("شهر: " + ad.getCity());
        cityLabel.setFont(new Font("B Yekan", 12));
        cityLabel.setStyle("-fx-text-fill: #757575;");

        Button detailsButton = new Button("مشاهده جزئیات");
        detailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
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

        card.getChildren().addAll(imageView, titleLabel, priceLabel, cityLabel, detailsButton);
        return card;
    }

    // متدهای مسیریابی (بدون تغییر)
    @FXML
    public void goToCreateAd(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/create-ad.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void goToFavorites(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/favorites.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void goToProfile(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/profile.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) { e.printStackTrace(); }
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

        Ad mock3 = new Ad(3L, "گوشی سامسونگ S23", 40000000L, "تهران", "");
        mock3.setDescription("در حد نو، دارای گارانتی");
        mock3.setCategory("الکترونیک");
        mock3.setSellerName("رضا محمدی");

        return new Ad[]{mock1, mock2, mock3};
    }
}