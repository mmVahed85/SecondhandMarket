package com.secondhand.controller;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.UpdateAdvertisementRequest;
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
import javafx.scene.layout.FlowPane;
import java.io.File;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.secondhand.dto.ImageResponse;

public class EditAdController {

    private final AdApi adApi = new AdApi();

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label messageLabel;
    @FXML private FlowPane imagesPane;

    private UpdateAdvertisementRequest request = new UpdateAdvertisementRequest();
    private AdvertisementResponse currentAd;

    @FXML
    public void initialize() {
        categoryComboBox.getItems().setAll(Category.values());
        categoryComboBox.getSelectionModel().clearSelection();
    }

    public void initData(AdvertisementResponse ad) {
        this.currentAd = ad;
        titleField.setText(ad.getTitle());
        priceField.setText(String.valueOf(ad.getPrice()));
        cityField.setText(ad.getCity() != null ? ad.getCity() : "");
        categoryComboBox.setValue(ad.getCategory());
        descriptionArea.setText(ad.getDescription() != null ? ad.getDescription() : "");
        loadImages();
    }

    private void loadImages() {
        imagesPane.getChildren().clear();

        if (currentAd.getImages() != null) {
            for (ImageResponse image : currentAd.getImages()) {
                ImageView preview = new ImageView();
                preview.setImage(new Image(image.getUrl(), true));
                preview.setFitWidth(120);
                preview.setFitHeight(120);
                preview.setPreserveRatio(true);

                Button deleteButton = new Button("✕");
                deleteButton.setStyle(
                        "-fx-background-color:#e53935;" +
                                "-fx-text-fill:white;" +
                                "-fx-background-radius:50%;"
                );

                deleteButton.setOnAction(e -> deleteImage(image));
                StackPane stack = new StackPane(preview, deleteButton);
                StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
                imagesPane.getChildren().add(stack);
            }
        }

        Button addButton = new Button("+");
        addButton.setPrefSize(120,120);
        addButton.setStyle(
                "-fx-font-size:30;" +
                        "-fx-background-color:#2196F3;" +
                        "-fx-text-fill:white;"
        );
        addButton.setOnAction(this::chooseImages);
        imagesPane.getChildren().add(addButton);
    }

    private void deleteImage(ImageResponse image){
        ApiResponse<String> response = adApi.deleteImage(image.getId());

        if(response.isSuccess()){
            currentAd.getImages().remove(image);
            loadImages();
        } else {
            messageLabel.setText(response.getMessage());
        }
    }

    @FXML
    private void chooseImages(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Images", "*.png", "*.jpg", "*.jpeg"
                )
        );

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        List<File> files = chooser.showOpenMultipleDialog(stage);

        if(files == null || files.isEmpty()) return;

        for(File file : files){
            ApiResponse<ImageResponse> response = adApi.uploadImage(currentAd.getId(), file);
            if(!response.isSuccess()){
                messageLabel.setText(response.getMessage());
            }
        }

        AdvertisementResponse updated = adApi.getAdvertisement(currentAd.getId()).getData();
        currentAd = updated;
        loadImages();
    }

    @FXML
    public void saveChanges(ActionEvent event) {
        try {
            if (!priceField.getText().isEmpty()) {
                long newPrice = Long.parseLong(priceField.getText());
                request.setPrice(newPrice);
            }
            if(titleField.getText()!= null) {
                request.setTitle(titleField.getText());
            }
            if(cityField.getText()!= null) {
                request.setCity(cityField.getText());
            }
            if(descriptionArea.getText()!= null) {
                request.setDescription(descriptionArea.getText());
            }
            if(categoryComboBox.getValue() != null) {
                request.setCategory(categoryComboBox.getValue());
            }

            ApiResponse<AdvertisementResponse> response = adApi.updateAd(currentAd.getId(), request);

            if(response.isSuccess()) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(response.getMessage());
                initData(response.getData());
            } else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(response.getMessage());
            }

        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please enter a valid numeric price.");
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/profile.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}