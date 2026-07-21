package com.secondhand.controller;

import com.secondhand.model.Ad;
import com.secondhand.model.User;
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

    @FXML private Label sectionTitleLabel;
    @FXML private Label messageLabel;

    // دکمه‌های عملیاتی
    @FXML private Button btnApprove;
    @FXML private Button btnReject;
    @FXML private Button btnDelete;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @FXML private TableView dataTable;

    private String currentSection = "ADS";

    private ObservableList<Ad> mockAds;
    private ObservableList<User> mockUsers;

    @FXML
    public void initialize() {
        mockAds = FXCollections.observableArrayList(
                new Ad(1L, "لپ‌تاپ ایسوس گیمینگ", 45000000L, "تهران", null),
                new Ad(2L, "دوچرخه کوهستان", 8500000L, "اصفهان", null),
                new Ad(3L, "گوشی سامسونگ S23", 40000000L, "شیراز", null)
        );
        mockAds.get(0).setSellerName("علی احمدی");
        mockAds.get(1).setSellerName("سارا رضایی");
        mockAds.get(2).setSellerName("رضا محمدی");

        mockUsers = FXCollections.observableArrayList(
                new User(101, "ali_ahmadi", "کاربر عادی", "فعال"),
                new User(102, "sara_rezaei", "کاربر عادی", "فعال"),
                new User(103, "admin_super", "مدیر سیستم", "فعال"),
                new User(104, "spammer_99", "کاربر عادی", "مسدود")
        );

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
        btnDelete.setText("🗑 حذف آگهی");

        dataTable.getColumns().clear();

        TableColumn<Ad, Long> idCol = new TableColumn<>("شناسه");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ad, String> titleCol = new TableColumn<>("عنوان آگهی");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Ad, Long> priceCol = new TableColumn<>("قیمت (تومان)");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Ad, String> sellerCol = new TableColumn<>("فروشنده");
        sellerCol.setCellValueFactory(new PropertyValueFactory<>("sellerName"));

        dataTable.getColumns().addAll(idCol, titleCol, priceCol, sellerCol);
        dataTable.setItems(mockAds);
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
        btnDelete.setText("🗑 حذف کاربر");

        dataTable.getColumns().clear();

        TableColumn<User, Integer> idCol = new TableColumn<>("شناسه کاربر");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> usernameCol = new TableColumn<>("نام کاربری");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("نقش کاربری");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> statusCol = new TableColumn<>("وضعیت");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        dataTable.getColumns().addAll(idCol, usernameCol, roleCol, statusCol);
        dataTable.setItems(mockUsers);
    }

    @FXML
    public void handleApprove(ActionEvent event) {
        Ad selectedAd = (Ad) dataTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            showError("لطفاً ابتدا یک آگهی را برای تایید انتخاب کنید.");
            return;
        }

        // در اینجا باید به سرور درخواست تغییر وضعیت ارسال شود
        System.out.println("درخواست تایید آگهی " + selectedAd.getId() + " به سرور ارسال شد.");
        showSuccess("آگهی '" + selectedAd.getTitle() + "' با موفقیت تایید شد و به کاربران نمایش داده می‌شود.");
    }

    @FXML
    public void handleReject(ActionEvent event) {
        Ad selectedAd = (Ad) dataTable.getSelectionModel().getSelectedItem();
        if (selectedAd == null) {
            showError("لطفاً ابتدا یک آگهی را برای رد کردن انتخاب کنید.");
            return;
        }

        // در اینجا باید به سرور درخواست رد ارسال شود و معمولاً آگهی از لیست ادمین هم حذف می‌شود
        mockAds.remove(selectedAd);
        System.out.println("درخواست رد آگهی " + selectedAd.getId() + " به سرور ارسال شد.");
        showSuccess("آگهی '" + selectedAd.getTitle() + "' رد شد و از سیستم حذف گردید.");
    }

    @FXML
    public void handleDelete(ActionEvent event) {
        Object selectedItem = dataTable.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showError("لطفاً ابتدا یک ردیف را از جدول انتخاب کنید.");
            return;
        }

        if (currentSection.equals("ADS")) {
            mockAds.remove((Ad) selectedItem);
            showSuccess("آگهی با موفقیت حذف شد.");
        } else if (currentSection.equals("USERS")) {
            mockUsers.remove((User) selectedItem);
            showSuccess("کاربر با موفقیت حذف شد.");
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