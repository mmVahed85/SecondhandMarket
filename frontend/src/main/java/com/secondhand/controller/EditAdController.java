package com.secondhand.controller;

import com.secondhand.model.Ad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditAdController {

    @FXML private TextField titleField;
    @FXML private TextField priceField;
    @FXML private TextField cityField;
    @FXML private TextField categoryField;
    @FXML private TextArea descriptionArea;
    @FXML private Label messageLabel;

    private Ad currentAd;

    public void initData(Ad ad) {
        this.currentAd = ad;
        titleField.setText(ad.getTitle());
        priceField.setText(String.valueOf(ad.getPrice()));
        cityField.setText(ad.getCity() != null ? ad.getCity() : "");
        categoryField.setText(ad.getCategory() != null ? ad.getCategory() : "");
        descriptionArea.setText(ad.getDescription() != null ? ad.getDescription() : "");
    }

    @FXML
    public void saveChanges(ActionEvent event) {
        if (titleField.getText().isEmpty() || priceField.getText().isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("عنوان و قیمت نمی‌توانند خالی باشند.");
            return;
        }

        try {
            long newPrice = Long.parseLong(priceField.getText());
            currentAd.setTitle(titleField.getText());
            currentAd.setPrice(newPrice);
            currentAd.setCity(cityField.getText());
            currentAd.setCategory(categoryField.getText());
            currentAd.setDescription(descriptionArea.getText());

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("تغییرات با موفقیت ذخیره شد!");

            System.out.println("آگهی ویرایش شد -> عنوان: " + currentAd.getTitle() + " | شهر: " + currentAd.getCity() + " | دسته: " + currentAd.getCategory());

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