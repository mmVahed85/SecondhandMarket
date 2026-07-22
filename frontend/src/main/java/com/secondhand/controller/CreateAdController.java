package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.model.*;
import com.secondhand.service.AdApi;
import com.secondhand.util.ApiConfig;
import com.secondhand.util.ApiResponse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class CreateAdController {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private TextField categoryField;
    @FXML private TextArea descriptionField;
    @FXML private Label messageLabel;

    // المان‌های مربوط به عکس
    @FXML private ImageView imageView;
    private String selectedImageBase64 = ""; // متغیری برای نگهداری عکس تبدیل شده

    private final AdApi adApi = new AdApi();

    // متد باز کردن پنجره انتخاب عکس
    @FXML
    public void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("انتخاب عکس آگهی");
        // محدود کردن انتخاب به فایل‌های تصویری
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // ۱. نمایش عکس در صفحه برای کاربر
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);

                // ۲. خواندن فایل و تبدیل آن به رشته Base64 برای ارسال به سرور
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                selectedImageBase64 = Base64.getEncoder().encodeToString(fileContent);

            } catch (Exception e) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("خطا در بارگذاری عکس.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleSubmit(ActionEvent event) {
        String title = titleField.getText();
        String priceStr = priceField.getText();
        String city = cityField.getText();
        Category category = Category.BOOK;
        String description = descriptionField.getText();

        if (title.isEmpty() || priceStr.isEmpty() || city.isEmpty() || description.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("لطفاً تمامی فیلدها را پر کنید.");
            return;
        }

        long price;
        try {
            price = Long.parseLong(priceStr);
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("لطفاً قیمت را به صورت عددی وارد کنید.");
            return;
        }

        // ارسال selectedImageBase64 به همراه سایر اطلاعات
        CreateAdvertisementRequest request = new CreateAdvertisementRequest(title, description, price, city, category);

        try {
            ApiResponse<AdvertisementResponse> response = adApi.createAd(request);
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("آگهی شما با موفقیت ثبت شد و در انتظار تایید مدیر است.");

            // خالی کردن فرم برای ثبت آگهی بعدی
            titleField.clear(); priceField.clear(); cityField.clear(); categoryField.clear(); descriptionField.clear();
            imageView.setImage(null);
            selectedImageBase64 = "";

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("خطا در ارتباط با سرور. لاگ‌ها را بررسی کنید.");
            e.printStackTrace();
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
}