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
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;

public class CreateAdController {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextArea descriptionField;
    @FXML private Label messageLabel;
    @FXML private FlowPane imagesPane;

    // المان‌های مربوط به عکس
    private List<File> selectedImageFiles = new ArrayList<>();

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

        fileChooser.setTitle("انتخاب تصاویر آگهی");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files",
                        "*.png",
                        "*.jpg",
                        "*.jpeg"
                )
        );


        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();


        List<File> files =
                fileChooser.showOpenMultipleDialog(stage);


        if (files != null && !files.isEmpty()) {

            selectedImageFiles.clear();

            selectedImageFiles.addAll(files);

            try {

                // فقط اولین عکس را پیش نمایش می‌دهد
                refreshImages();

                messageLabel.setStyle("-fx-text-fill:green;");
                messageLabel.setText(
                        files.size() + " عکس انتخاب شد."
                );

            }

            catch (Exception e) {

                messageLabel.setStyle("-fx-text-fill:red;");
                messageLabel.setText("خطا در بارگذاری عکس");

            }

        }

    }

    private void refreshImages() {

        imagesPane.getChildren().clear();

        for (File file : selectedImageFiles) {

            ImageView preview = new ImageView(
                    new Image(file.toURI().toString())
            );

            preview.setFitWidth(100);
            preview.setFitHeight(100);
            preview.setPreserveRatio(true);

            Button remove = new Button("✖");

            remove.setStyle("""
                    -fx-background-color:red;
                    -fx-text-fill:white;
                    -fx-background-radius:50%;
                    """);

            remove.setOnAction(e -> {

                selectedImageFiles.remove(file);

                refreshImages();

            });

            StackPane stack = new StackPane(preview, remove);

            StackPane.setAlignment(remove, javafx.geometry.Pos.TOP_RIGHT);

            imagesPane.getChildren().add(stack);

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

                if (!selectedImageFiles.isEmpty()) {

                    boolean uploadFailed = false;

                    for (File imageFile : selectedImageFiles) {

                        ApiResponse<ImageResponse> imageResponse =
                                adApi.uploadImage(advertisementId, imageFile);

                        if (!imageResponse.isSuccess()) {
                            uploadFailed = true;
                        }
                    }
                    if (uploadFailed) {
                        messageLabel.setStyle("-fx-text-fill: orange;");
                        messageLabel.setText("آگهی ثبت شد ولی برخی از عکس‌ها آپلود نشدند.");
                    } else {
                        messageLabel.setStyle("-fx-text-fill: green;");
                        messageLabel.setText("آگهی و همه تصاویر با موفقیت ثبت شدند.");
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
                selectedImageFiles.clear();
                imagesPane.getChildren().clear();
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