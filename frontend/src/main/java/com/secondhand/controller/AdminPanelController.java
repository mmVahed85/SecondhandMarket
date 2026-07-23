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

    // المان‌های فیلتر و جزئیات
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private Button btnViewDetails;

    // دکمه‌های عملیاتی
    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnDelete;
    @FXML private Button btnBlock;
    @FXML private Button btnUnblock;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @FXML private TableView dataTable;

    private String currentSection = "ADS";

    // لیست کمکی برای فیلتر کردن بدون نیاز به ریکوئست مجدد به سرور
    private List<AdvertisementResponse> allAdsCache;

    @FXML
    public void initialize() {
        // پر کردن فیلتر وضعیت به صورت داینامیک از روی Enum بک‌اند
        statusFilterComboBox.getItems().add("همه");
        for (AdvertisementStatus status : AdvertisementStatus.values()) {
            statusFilterComboBox.getItems().add(status.name());
        }
        statusFilterComboBox.setValue("همه");

        showAdsManager(null);
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void showAdsManager(ActionEvent event) {
        currentSection = "ADS";
        sectionTitleLabel.setText("مدیریت آگهی‌ها");
        messageLabel.setText("");

        // مدیریت نمایش دکمه‌ها
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

        // ستون وضعیت با نوع داده Enum
        TableColumn<AdvertisementResponse, AdvertisementStatus> statusCol = new TableColumn<>("وضعیت");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        dataTable.getColumns().addAll(idCol, titleCol, priceCol, sellerCol, statusCol);

        // درخواست از سرور
        // توجه: اگر متد getAllAdvertisements را در بک‌اند ساختید، آن را جایگزین getPendingAdvertisements کنید
        ApiResponse<List<AdvertisementResponse>> response = adminApi.getAllAdvertisements();

        if (response.isSuccess()) {
            allAdsCache = response.getData();
            dataTable.setItems(FXCollections.observableArrayList(allAdsCache));
            handleFilterAds(null); // اعمال فیلتر اولیه (در صورتی که روی چیزی تنظیم شده باشد)
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

        if (selectedStatus == null || selectedStatus.equals("همه")) {
            dataTable.setItems(FXCollections.observableArrayList(allAdsCache));
        } else {
            // مقایسه نام Enum ذخیره شده در آگهی با وضعیت انتخاب شده در منوی کشویی
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
            showError("لطفاً ابتدا یک آگهی را انتخاب کنید.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ad-details.fxml"));
            Parent root = loader.load();

            // این دو خط، اطلاعات آگهی را به صفحه جزئیات ارسال می‌کنند
            AdDetailsController controller = loader.getController();
            controller.setAd(selectedAd);

            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError("خطا در باز کردن صفحه جزئیات آگهی.");
        }
    }

    @FXML
    public void handleApprove(ActionEvent event) {
        AdvertisementResponse selectedAd = (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();
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
        AdvertisementResponse selectedAd = (AdvertisementResponse) dataTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            showError("لطفاً ابتدا یک آگهی را انتخاب کنید.");
            return;
        }
        ApiResponse<AdvertisementResponse> response = adminApi.reject(selectedAd.getId());
        if (response.isSuccess()) {
            showSuccess("آگهی رد شد.");
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
            showError("یک کاربر انتخاب کنید.");
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
            showError("یک کاربر انتخاب کنید.");
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