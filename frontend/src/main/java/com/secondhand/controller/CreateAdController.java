package com.secondhand.controller;

//import java.time.LocalDateTime;
import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.CreateAdvertisementRequest;
import com.secondhand.entity.Category;
import com.secondhand.service.AdApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class CreateAdController {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private ComboBox<Category> categoryComboBox; // تغییر به ComboBox برای هماهنگی با Enum بک‌اند
    @FXML private TextArea descriptionField;
    @FXML private Label messageLabel;

    @FXML private ImageView imageView;
    private File selectedImageFile; // نگه داشتن فایل برای آپلود مرحله دوم

    private final AdApi adApi = new AdApi();

    @FXML
    public void initialize() {
        // پر کردن ComboBox با مقادیر استاندارد Category از بک‌اند
        if (categoryComboBox != null) {
            categoryComboBox.getItems().addAll(Arrays.asList(Category.values()));
        }
    }

    @FXML
    public void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("انتخاب عکس آگهی");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                imageView.setImage(image);
            } catch (Exception e) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("خطا در بارگذاری پیش‌نمایش عکس.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleSubmitAd(ActionEvent event) {
        try {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String priceStr = priceField.getText();
            String city = cityField.getText();
            Category category = categoryComboBox.getValue();

            if (title == null || title.isBlank() || priceStr == null || priceStr.isBlank() || city == null || city.isBlank() || category == null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("لطفاً تمامی فیلدهای ضروری را پر کنید.");
                return;
            }

            Long price = Long.parseLong(priceStr);

            // ۱. ساخت درخواست ایجاد آگهی (بدون عکس)
            CreateAdvertisementRequest request = new CreateAdvertisementRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setPrice(price);
            request.setCity(city);
            request.setCategory(category);

            AdvertisementResponse createdAd = adApi.createAd(request);

            if (createdAd != null && createdAd.getId() != null) {

                // ۲. اگر کاربر عکسی انتخاب کرده باشد، آن را در مرحله دوم با متد اختصاصی آپلود می‌کنیم
                if (selectedImageFile != null) {
                    boolean imageUploaded = adApi.uploadAdImage(createdAd.getId(), selectedImageFile);
                    if (!imageUploaded) {
                        System.err.println("هشدار: آگهی ساخته شد اما آپلود عکس با خطا مواجه شد.");
                    }
                }

                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("آگهی با موفقیت ثبت شد و در انتظار تایید است.");

                // بازگشت به داشبورد پس از ثبت موفق
                javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
                pause.setOnFinished(e -> goBack(event));
                pause.play();
            }

        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("لطفاً قیمت را به صورت عدد صحیح وارد کنید.");
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(e.getMessage());
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