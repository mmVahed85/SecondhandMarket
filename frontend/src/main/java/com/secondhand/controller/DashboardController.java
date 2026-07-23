package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.model.*;
import com.secondhand.service.AdApi;
import com.secondhand.util.ApiConfig;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private TilePane adsContainer;

    @FXML private Button adminBT;
    
    // المان‌های فیلتر
    @FXML private TextField searchField;
    @FXML private TextField cityFilterField;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private ComboBox<SortType> sortTypeComboBox;

    private final AdApi adApi = new AdApi();

    // یک لیست برای نگهداری تمام آگهی‌های دریافتی از سرور (برای سرعت در فیلتر کردن)

    @FXML
    public void initialize() {
        loadAdsFromServer();
        categoryComboBox.getItems().setAll(Category.values());
        sortTypeComboBox.getItems().setAll(SortType.values());
        categoryComboBox.getSelectionModel().clearSelection();
        sortTypeComboBox.getSelectionModel().clearSelection();
        if(SessionManager.getRole().equals("ADMIN")) {
            adminBT.setVisible(true);
            adminBT.setManaged(true);
        }
        else {
            adminBT.setVisible(false);
            adminBT.setManaged(false);
        }
    }

    private void loadAdsFromServer() {
        ApiResponse<List<AdvertisementResponse>> adResponses = adApi.getActiveAds();

        if (adResponses == null || adResponses.getData().size() == 0) {
            System.out.println("دریافت از سرور خالی بود. بارگذاری داده‌های تستی...");
        }

        // نمایش همه آگهی‌ها در ابتدا
        displayAds(adResponses.getData());
    }

    // متد جدید برای نمایش یک لیست مشخص از آگهی‌ها
    private void displayAds(List<AdvertisementResponse> adResponses) {

        adsContainer.getChildren().clear();

        if (adResponses == null || adResponses.isEmpty()) {

            Label noAdLabel = new Label("هیچ آگهی‌ای یافت نشد.");
            noAdLabel.setFont(new Font("B Yekan",18));

            adsContainer.getChildren().add(noAdLabel);

            return;
        }

        for (AdvertisementResponse ad : adResponses) {

            adsContainer.getChildren().add(createAdCard(ad));

        }
    }

    // متد اعمال فیلترها (هنگام کلیک روی دکمه جستجو)
    @FXML
    public void handleFilter(ActionEvent event) {

        AdvertisementFilterRequest request = new AdvertisementFilterRequest();

        request.setCity(cityFilterField.getText().trim());
        request.setKeyword(searchField.getText().trim());
        if (categoryComboBox.getValue() != null) {

            request.setCategory(categoryComboBox.getValue());

        }
        if (sortTypeComboBox.getValue() != null) {

            request.setSortType(sortTypeComboBox.getValue());

        }
        if (!minPriceField.getText().isBlank()) {
            request.setMinPrice(Long.parseLong(minPriceField.getText()));
        }

        if (!maxPriceField.getText().isBlank()) {
            request.setMaxPrice(Long.parseLong(maxPriceField.getText()));
        }

        ApiResponse<List<AdvertisementResponse>> response = adApi.filterAdvertisements(request);

        if (response.isSuccess()) {

            displayAds(response.getData());

        } else {

            Label noAdLabel = new Label(response.getMessage());
            noAdLabel.setFont(new Font("B Yekan", 18));
            System.out.println(response.getMessage());

        }
    }

    // متد پاک کردن فیلترها
    @FXML
    public void clearFilters(ActionEvent event) {
        searchField.clear();
        cityFilterField.clear();
        minPriceField.clear();
        maxPriceField.clear();

        categoryComboBox.getSelectionModel().clearSelection();
        sortTypeComboBox.getSelectionModel().clearSelection();

        loadAdsFromServer();
    }

    // متد ساخت ظاهر کارت آگهی (بدون تغییر نسبت به قبل)
    private VBox createAdCard(AdvertisementResponse ad) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 300);

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
    public void goToAdminPanel(ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/admin-panel.fxml"));
            javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception ex) {
            System.err.println("خطا در بارگذاری پنل مدیریت:");
            ex.printStackTrace();
        }
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
}