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

public class EditAdController {

    private final AdApi adApi = new AdApi();

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextArea descriptionArea;
    @FXML private Label messageLabel;

    private UpdateAdvertisementRequest request;
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
            }
            else {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText(response.getMessage());
            }

        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("لطفاً قیمت را به صورت عدد وارد کنید.");
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