package com.secondhand.controller;

import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.service.AuthApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    // اتصال المان‌های گرافیکی FXML به متغیرهای جاوا
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthApi authApi = new AuthApi();

    // این متد با کلیک روی دکمه "ورود" اجرا می‌شود
    @FXML
    public void handleLogin(ActionEvent event) {
        // ۱. گرفتن مقادیر تایپ شده توسط کاربر
        String username = usernameField.getText();
        String password = passwordField.getText();

        // ۲. اعتبارسنجی فرانت‌اند: چک کردن خالی نبودن فیلدها
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("لطفاً نام کاربری و رمز عبور را وارد کنید.");
            return; // توقف اجرای کد تا زمانی که فیلدها پر شوند
        }

        // ۳. ساخت آبجکت درخواست و ارسال به بک‌اند
        LoginRequest request = new LoginRequest(username, password);

        try {
            LoginResponse response = authApi.login(request);

            // ۴. بررسی پاسخ سرور
            if (response.isSuccess()) {
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("ورود موفق! توکن دریافت شد.");
                System.out.println("Token: " + response.getToken());

                // قدم بعدی: ذخیره توکن در SessionManager و رفتن به صفحه آگهی‌ها
            } else {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(response.getMessage()); // نمایش پیام خطای سرور
            }
        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("خطا در ارتباط با سرور!");
        }
    }

    @FXML
    public void goToRegister(javafx.event.ActionEvent event) {
        try {
            // ۱. بارگذاری فایل گرافیکی صفحه ثبت‌نام
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/register.fxml"));

            // ۲. پیدا کردن صحنه (Scene) فعلی از روی لینکی که کلیک شده
            javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();

            // ۳. تغییر محتوای صحنه (بدون تغییر دادن خود پنجره و تنظیمات فول‌اسکرین)
            currentScene.setRoot(root);

        } catch (Exception e) {
            System.err.println("خطا در بارگذاری صفحه ثبت‌نام:");
            e.printStackTrace();
        }
    }
}