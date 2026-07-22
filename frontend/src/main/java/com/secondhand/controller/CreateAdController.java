package com.secondhand.controller;

import com.secondhand.dto.*;
import com.secondhand.model.*;
import com.secondhand.service.AdApi;
import com.secondhand.util.ApiResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class CreateAdController {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextArea descriptionField;
    @FXML private Label messageLabel;

    // المان‌های مربوط به عکس
    @FXML private ImageView imageView;
    private File selectedImageFile;

    private final AdApi adApi = new AdApi();

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll(Category.values());
        categoryComboBox.getSelectionModel().clearSelection();
    }
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

        selectedImageFile = selectedFile;

        if (selectedFile != null) {
            try {

                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);

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
        Category category = categoryComboBox.getValue();
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
            if(response.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(response.getMessage());
                Long advertisementId = response.getData().getId();
                System.out.println(response.getMessage());

                if (selectedImageFile != null) {

                    ApiResponse<ImageResponse> imageResponse = adApi.uploadImage(advertisementId, selectedImageFile);
                    System.out.println(imageResponse.getMessage());
                    if (!imageResponse.isSuccess()) {

                        messageLabel.setStyle("-fx-text-fill: orange;");
                        messageLabel.setText(
                                "آگهی ثبت شد ولی عکس آپلود نشد."
                        );

                    }
                }
            }
            else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(response.getMessage());
            }

            if(response.isSuccess()) {
                // خالی کردن فرم برای ثبت آگهی بعدی
                titleField.clear(); priceField.clear(); cityField.clear(); categoryComboBox.getSelectionModel().clearSelection(); descriptionField.clear();
                selectedImageFile = null;
                imageView.setImage(null);
            }

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