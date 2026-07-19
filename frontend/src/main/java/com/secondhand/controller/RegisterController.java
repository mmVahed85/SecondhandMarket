package com.secondhand.controller;

import com.secondhand.model.RegisterRequest;
import com.secondhand.service.AuthApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthApi authApi = new AuthApi();

    @FXML
    public void handleRegister(ActionEvent event) {
        String firstName = firstNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        // اعتبارسنجی فرانت‌اند: چک کردن خالی نبودن فیلدها
        if (firstName == null || firstName.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("لطفاً تمامی فیلدها را پر کنید.");
            return;
        }

        // ساخت آبجکت درخواست
        RegisterRequest request = new RegisterRequest();
        request.setFirstName(firstName);
        request.setLastName(""); // چون در فرم UI نام خانوادگی نداشتیم، خالی می‌فرستیم
        request.setUsername(username);
        request.setEmail(email);
        request.setPhone(phone);
        request.setPassword(password);

        try {
            // ارسال درخواست به بک‌اند
            String response = authApi.register(request);

            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("ثبت‌نام با موفقیت انجام شد.");
            System.out.println("Server Response: " + response);

            // قدم بعدی: انتقال خودکار کاربر به صفحه ورود
        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("خطا در ارتباط با سرور!");
            e.printStackTrace();
        }
    }

    @FXML
    public void goToLogin(javafx.event.ActionEvent event) {
        try {
            // ۱. بارگذاری فایل گرافیکی صفحه ورود
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/login.fxml"));

            // ۲. پیدا کردن صحنه (Scene) فعلی از روی لینکی که کلیک شده
            javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();

            // ۳. تغییر محتوای صحنه (بدون تغییر دادن خود پنجره و تنظیمات فول‌اسکرین)
            currentScene.setRoot(root);

        } catch (Exception e) {
            System.err.println("خطا در بارگذاری صفحه ورود:");
            e.printStackTrace();
        }
    }
}