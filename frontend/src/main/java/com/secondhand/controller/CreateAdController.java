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

    private List<File> selectedImageFiles = new ArrayList<>();
    private final AdApi adApi = new AdApi();

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll(Category.values());
        categoryComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Ad Images");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        List<File> files = fileChooser.showOpenMultipleDialog(stage);

        if (files != null && !files.isEmpty()) {
            selectedImageFiles.clear();
            selectedImageFiles.addAll(files);
            try {
                refreshImages();
                messageLabel.setStyle("-fx-text-fill:green;");
                messageLabel.setText(files.size() + " image(s) selected.");
            } catch (Exception e) {
                messageLabel.setStyle("-fx-text-fill:red;");
                messageLabel.setText("Error loading images.");
            }
        }
    }

    private void refreshImages() {
        imagesPane.getChildren().clear();
        for (File file : selectedImageFiles) {
            ImageView preview = new ImageView(new Image(file.toURI().toString()));
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
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        long price;
        try {
            price = Long.parseLong(priceStr);
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid numeric price.");
            return;
        }

        CreateAdvertisementRequest request = new CreateAdvertisementRequest(title, description, price, city, category);

        try {
            ApiResponse<AdvertisementResponse> response = adApi.createAd(request);
            if(response.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(response.getMessage());
                Long advertisementId = response.getData().getId();

                if (!selectedImageFiles.isEmpty()) {
                    boolean uploadFailed = false;
                    for (File imageFile : selectedImageFiles) {
                        ApiResponse<ImageResponse> imageResponse = adApi.uploadImage(advertisementId, imageFile);
                        if (!imageResponse.isSuccess()) {
                            uploadFailed = true;
                        }
                    }
                    if (uploadFailed) {
                        messageLabel.setStyle("-fx-text-fill: orange;");
                        messageLabel.setText("Ad created, but some images failed to upload.");
                    } else {
                        messageLabel.setStyle("-fx-text-fill: green;");
                        messageLabel.setText("Ad and images successfully created.");
                    }
                }
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(response.getMessage());
            }

            if(response.isSuccess()) {
                titleField.clear(); priceField.clear(); cityField.clear();
                categoryComboBox.getSelectionModel().clearSelection(); descriptionField.clear();
                selectedImageFiles.clear();
                imagesPane.getChildren().clear();
            }

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Server connection error. Check logs.");
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