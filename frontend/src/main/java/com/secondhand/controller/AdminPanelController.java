package com.secondhand.controller;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.service.AdApi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdminPanelController {

    @FXML private TableView<AdvertisementResponse> pendingTable;
    @FXML private TableColumn<AdvertisementResponse, Long> idColumn;
    @FXML private TableColumn<AdvertisementResponse, String> titleColumn;
    @FXML private TableColumn<AdvertisementResponse, String> ownerColumn;
    @FXML private TableColumn<AdvertisementResponse, String> cityColumn;
    @FXML private Label messageLabel;

    private final AdApi adApi = new AdApi();
    private final ObservableList<AdvertisementResponse> pendingAdsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // تنظیم ستون‌های جدول پنل ادمین
        if (idColumn != null && titleColumn != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            ownerColumn.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));
            cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

            loadPendingAds();
        }
    }

    @FXML
    public void loadPendingAds() {
        try {
            List<AdvertisementResponse> pendingAds = adApi.getPendingAds();
            pendingAdsList.clear();
            if (pendingAds != null) {
                pendingAdsList.addAll(pendingAds);
                pendingTable.setItems(pendingAdsList);
            }
        } catch (Exception e) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("خطا در بارگذاری آگهی‌های در انتظار تایید.");
            }
            e.printStackTrace();
        }
    }

    @FXML
    public void handleApproveAd(ActionEvent event) {
        AdvertisementResponse selectedAd = pendingTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("لطفاً یک آگهی را از جدول انتخاب کنید.");
            }
            return;
        }

        boolean success = adApi.approveAd(selectedAd.getId());
        if (success) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("آگهی با موفقیت تایید شد.");
            }
            loadPendingAds(); // بارگذاری مجدد لیست
        } else {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("خطا در تایید آگهی.");
            }
        }
    }

    @FXML
    public void handleRejectAd(ActionEvent event) {
        AdvertisementResponse selectedAd = pendingTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("لطفاً یک آگهی را از جدول انتخاب کنید.");
            }
            return;
        }

        boolean success = adApi.rejectAd(selectedAd.getId());
        if (success) {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("آگهی با موفقیت رد شد.");
            }
            loadPendingAds(); // بارگذاری مجدد لیست
        } else {
            if (messageLabel != null) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("خطا در رد آگهی.");
            }
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            com.secondhand.util.SessionManager.logout();
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}