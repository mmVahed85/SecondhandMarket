package com.secondhand.controller;

import java.util.List;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.UpdateAdvertisementRequest;
import com.secondhand.dto.UserResponse;
import com.secondhand.model.*;
import com.secondhand.service.AdApi;
import com.secondhand.service.AdminApi;
import com.secondhand.util.ApiResponse;
import com.secondhand.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelController {

    private final AdminApi adminApi = new AdminApi();

    @FXML private Label sectionTitleLabel;
    @FXML private Label messageLabel;

    // دکمه‌های عملیاتی
    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnDelete;
    @FXML private Button btnBlock;
    @FXML private Button btnUnblock;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @FXML private TableView dataTable;

    private String currentSection = "ADS";

    private ObservableList<AdvertisementResponse> mockAds;
    private ObservableList<UserResponse> mockUsers;

    @FXML
    public void initialize() {
        showAdsManager(null);
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void showAdsManager(ActionEvent event) {
        currentSection = "ADS";
        sectionTitleLabel.setText("مدیریت آگهی‌ها");
        messageLabel.setText("");

        // نمایش دادن دکمه‌های تایید و رد در بخش آگهی‌ها
        btnApprove.setVisible(true);
        btnApprove.setManaged(true);
        btnReject.setVisible(true);
        btnReject.setManaged(true);
        btnBlock.setVisible(false);
        btnBlock.setManaged(false);
        btnUnblock.setVisible(false);
        btnUnblock.setManaged(false);
        btnDelete.setText("🗑 حذف آگهی");

        dataTable.getColumns().clear();

        TableColumn<AdvertisementResponse, Long> idCol = new TableColumn<>("شناسه");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<AdvertisementResponse, String> titleCol = new TableColumn<>("عنوان آگهی");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<AdvertisementResponse, Long> priceCol = new TableColumn<>("قیمت (تومان)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<AdvertisementResponse, String> sellerCol = new TableColumn<>("فروشنده");
        sellerCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));

        dataTable.getColumns().addAll(idCol, titleCol, priceCol, sellerCol);
        
        ApiResponse<List<AdvertisementResponse>> response = adminApi.getPendingAdvertisements();

        if (response.isSuccess()) {

            ObservableList<AdvertisementResponse> ads = FXCollections.observableArrayList(response.getData());

            dataTable.setItems(ads);

        } else {

            showError(response.getMessage());

        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void showUsersManager(ActionEvent event) {
        currentSection = "USERS";
        sectionTitleLabel.setText("مدیریت کاربران");
        messageLabel.setText("");

        // مخفی کردن دکمه‌های تایید و رد در بخش کاربران
        btnApprove.setVisible(false);
        btnApprove.setManaged(false);
        btnReject.setVisible(false);
        btnReject.setManaged(false);
        btnBlock.setVisible(true);
        btnBlock.setManaged(true);
        btnUnblock.setVisible(true);
        btnUnblock.setManaged(true);
        btnDelete.setText("🗑 حذف کاربر");

        dataTable.getColumns().clear();

        TableColumn<UserResponse, Integer> idCol = new TableColumn<>("شناسه کاربر");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<UserResponse, String> usernameCol = new TableColumn<>("نام کاربری");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserResponse, String> roleCol = new TableColumn<>("نقش کاربری");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<UserResponse, String> statusCol = new TableColumn<>("وضعیت");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        dataTable.getColumns().addAll(idCol, usernameCol, roleCol, statusCol);
        
        ApiResponse<List<UserResponse>> response = adminApi.getUsers();

            if (response.isSuccess()) {

                ObservableList<UserResponse> ads = FXCollections.observableArrayList(response.getData());

                dataTable.setItems(ads);

            } else {

                showError(response.getMessage());

            }
        }

    @FXML
    public void handleApprove(ActionEvent event) {

        AdvertisementResponse selectedAd =
                (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();

        if (selectedAd == null) {
            showError("لطفاً ابتدا یک آگهی را انتخاب کنید.");
            return;
        }

        ApiResponse<AdvertisementResponse> response = adminApi.approve(selectedAd.getId());

        if (response.isSuccess()) {

            showSuccess(response.getMessage());

            showAdsManager(null);

        } else {

            showError(response.getMessage());

        }
    }

    @FXML
    public void handleReject(ActionEvent event) {

        AdvertisementResponse selectedAd =
                (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();

        if (selectedAd == null) {
            showError("لطفاً ابتدا یک آگهی را انتخاب کنید.");
            return;
        }

        ApiResponse<AdvertisementResponse> response =
                adminApi.reject(selectedAd.getId());

        if (response.isSuccess()) {

            showSuccess("آگهی حذف شد.");

            showAdsManager(null);

        } else {

            showError(response.getMessage());

        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {

        Object selectedItem = dataTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("لطفاً ابتدا یک ردیف را از جدول انتخاب کنید.");
            return;
        }

        if (currentSection.equals("ADS")) {
            
            ApiResponse<AdvertisementResponse> response = adminApi.deleteAd(((AdvertisementResponse) selectedItem).getId());
            if (response.isSuccess()) {
                showSuccess(response.getMessage());
                showAdsManager(null);
            }
            else {
                showError(response.getMessage());
            }
        } else if (currentSection.equals("USERS")) {
            ApiResponse<String> response = adminApi.deleteUser(((UserResponse) selectedItem).getId());
            if (response.isSuccess()) {
                showSuccess(response.getMessage());
                showUsersManager(null);
            }
            else {
                showError(response.getMessage());
            }
        }
    }

    @FXML
    public void handleBlock(ActionEvent event) {

        UserResponse user =
            (UserResponse) dataTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            showError("یک کاربر انتخاب کنید.");
            return;
        }

        ApiResponse<String> response =
                adminApi.blockUser(user.getId());

        if (response.isSuccess()) {

            showSuccess(response.getMessage());
            showUsersManager(null);

        } else {

            showError(response.getMessage());

        }
    }

    @FXML
    public void handleUnblock(ActionEvent event) {

        UserResponse user =
            (UserResponse) dataTable.getSelectionModel().getSelectedItem();

        if (user == null) {
            showError("یک کاربر انتخاب کنید.");
            return;
        }

        ApiResponse<String> response =
                adminApi.unblockUser(user.getId());

        if (response.isSuccess()) {

            showSuccess(response.getMessage());
            showUsersManager(null);

        } else {

            showError(response.getMessage());

        }
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;"); // رنگ سبز
        messageLabel.setText(message);
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;"); // رنگ قرمز
        messageLabel.setText("خطا: " + message);
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        SessionManager.logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}