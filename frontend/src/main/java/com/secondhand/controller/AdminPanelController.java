package com.secondhand.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.UserResponse;
import com.secondhand.model.AdvertisementStatus;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminPanelController {

    private final AdminApi adminApi = new AdminApi();

    @FXML private Label sectionTitleLabel;
    @FXML private Label messageLabel;

    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private Button btnViewDetails;

    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnDelete;
    @FXML private Button btnBlock;
    @FXML private Button btnUnblock;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @FXML private TableView dataTable;

    private String currentSection = "ADS";
    private List<AdvertisementResponse> allAdsCache;

    @FXML
    public void initialize() {
        statusFilterComboBox.getItems().add("All");
        for (AdvertisementStatus status : AdvertisementStatus.values()) {
            statusFilterComboBox.getItems().add(status.name());
        }
        statusFilterComboBox.setValue("All");

        showAdsManager(null);
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void showAdsManager(ActionEvent event) {
        currentSection = "ADS";
        sectionTitleLabel.setText("Manage Ads");
        messageLabel.setText("");

        statusFilterComboBox.setVisible(true);
        statusFilterComboBox.setManaged(true);
        btnViewDetails.setVisible(true);
        btnViewDetails.setManaged(true);
        btnApprove.setVisible(true);
        btnApprove.setManaged(true);
        btnReject.setVisible(true);
        btnReject.setManaged(true);

        btnBlock.setVisible(false);
        btnBlock.setManaged(false);
        btnUnblock.setVisible(false);
        btnUnblock.setManaged(false);
        btnDelete.setText("🗑 Delete Ad");

        dataTable.getColumns().clear();

        TableColumn<AdvertisementResponse, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<AdvertisementResponse, String> titleCol = new TableColumn<>("Ad Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<AdvertisementResponse, Long> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<AdvertisementResponse, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));

        TableColumn<AdvertisementResponse, AdvertisementStatus> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        dataTable.getColumns().addAll(idCol, titleCol, priceCol, sellerCol, statusCol);

        ApiResponse<List<AdvertisementResponse>> response = adminApi.getAllAdvertisements();

        if (response.isSuccess()) {
            allAdsCache = response.getData();
            dataTable.setItems(FXCollections.observableArrayList(allAdsCache));
            handleFilterAds(null);
        } else {
            showError(response.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void showUsersManager(ActionEvent event) {
        currentSection = "USERS";
        sectionTitleLabel.setText("Manage Users");
        messageLabel.setText("");

        statusFilterComboBox.setVisible(false);
        statusFilterComboBox.setManaged(false);
        btnViewDetails.setVisible(false);
        btnViewDetails.setManaged(false);
        btnApprove.setVisible(false);
        btnApprove.setManaged(false);
        btnReject.setVisible(false);
        btnReject.setManaged(false);

        btnBlock.setVisible(true);
        btnBlock.setManaged(true);
        btnUnblock.setVisible(true);
        btnUnblock.setManaged(true);
        btnDelete.setText("🗑 Delete User");

        dataTable.getColumns().clear();

        TableColumn<UserResponse, Integer> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<UserResponse, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<UserResponse, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<UserResponse, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        dataTable.getColumns().addAll(idCol, usernameCol, roleCol, statusCol);

        ApiResponse<List<UserResponse>> response = adminApi.getUsers();

        if (response.isSuccess()) {
            ObservableList<UserResponse> users = FXCollections.observableArrayList(response.getData());
            dataTable.setItems(users);
        } else {
            showError(response.getMessage());
        }
    }

    @FXML
    public void handleFilterAds(ActionEvent event) {
        if (allAdsCache == null) return;

        String selectedStatus = statusFilterComboBox.getValue();

        if (selectedStatus == null || selectedStatus.equals("All")) {
            dataTable.setItems(FXCollections.observableArrayList(allAdsCache));
        } else {
            List<AdvertisementResponse> filtered = allAdsCache.stream()
                    .filter(ad -> ad.getStatus() != null && selectedStatus.equals(ad.getStatus().name()))
                    .collect(Collectors.toList());
            dataTable.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    @FXML
    public void handleViewDetails(ActionEvent event) {
        AdvertisementResponse selectedAd = (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();

        if (selectedAd == null) {
            showError("Please select an ad first.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ad-details.fxml"));
            Parent root = loader.load();

            AdDetailsController controller = loader.getController();
            controller.setAd(selectedAd);
            controller.setPreviousPage("/view/admin-panel.fxml");

            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error opening ad details page.");
        }
    }

    @FXML
    public void handleApprove(ActionEvent event) {
        AdvertisementResponse selectedAd = (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            showError("Please select an ad first.");
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
        AdvertisementResponse selectedAd = (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            showError("Please select an ad first.");
            return;
        }
        ApiResponse<AdvertisementResponse> response = adminApi.reject(selectedAd.getId());
        if (response.isSuccess()) {
            showSuccess("Ad rejected.");
            showAdsManager(null);
        } else {
            showError(response.getMessage());
        }
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        Object selectedItem = dataTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showError("Please select a row from the table first.");
            return;
        }

        if (currentSection.equals("ADS")) {
            ApiResponse<AdvertisementResponse> response = adminApi.deleteAd(((AdvertisementResponse) selectedItem).getId());
            if (response.isSuccess()) {
                showSuccess(response.getMessage());
                showAdsManager(null);
            } else {
                showError(response.getMessage());
            }
        } else if (currentSection.equals("USERS")) {
            ApiResponse<String> response = adminApi.deleteUser(((UserResponse) selectedItem).getId());
            if (response.isSuccess()) {
                showSuccess(response.getMessage());
                showUsersManager(null);
            } else {
                showError(response.getMessage());
            }
        }
    }

    @FXML
    public void handleBlock(ActionEvent event) {
        UserResponse user = (UserResponse) dataTable.getSelectionModel().getSelectedItem();
        if (user == null) {
            showError("Please select a user.");
            return;
        }
        ApiResponse<String> response = adminApi.blockUser(user.getId());
        if (response.isSuccess()) {
            showSuccess(response.getMessage());
            showUsersManager(null);
        } else {
            showError(response.getMessage());
        }
    }

    @FXML
    public void handleUnblock(ActionEvent event) {
        UserResponse user = (UserResponse) dataTable.getSelectionModel().getSelectedItem();
        if (user == null) {
            showError("Please select a user.");
            return;
        }
        ApiResponse<String> response = adminApi.unblockUser(user.getId());
        if (response.isSuccess()) {
            showSuccess(response.getMessage());
            showUsersManager(null);
        } else {
            showError(response.getMessage());
        }
    }

    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: #27ae60;");
        messageLabel.setText(message);
    }

    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText("Error: " + message);
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