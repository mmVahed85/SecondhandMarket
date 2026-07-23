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
    @FXML private TextField lastNameField; // متغیر جدید
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final AuthApi authApi = new AuthApi();

    @FXML
    public void handleRegister(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText(); // گرفتن مقدار
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        // اعتبارسنجی فرانت‌اند: چک کردن خالی نبودن فیلدها (نام خانوادگی هم اضافه شد)
        if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("لطفاً تمامی فیلدها را پر کنید.");
            return;
        }

        // ساخت آبجکت درخواست
        RegisterRequest request = new RegisterRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName); // ست کردن نام خانوادگی
        request.setUsername(username);
        request.setEmail(email);
        request.setPhone(phone);
        request.setPassword(password);

        try {
            // ارسال درخواست به بک‌اند
            String response = authApi.register(request);

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("ثبت‌نام با موفقیت انجام شد.");
            System.out.println("Server Response: " + response);

        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("خطا در ارتباط با سرور!");
            e.printStackTrace();
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        try {
            // بارگذاری فایل گرافیکی صفحه ورود
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/login.fxml"));

            // پیدا کردن صحنه (Scene) فعلی از روی لینکی که کلیک شده
            javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();

            // تغییر محتوای صحنه (برای جلوگیری از به هم ریختن فول اسکرین)
            currentScene.setRoot(root);

        } catch (Exception e) {
            System.err.println("خطا در بارگذاری صفحه ورود:");
            e.printStackTrace();
        }
    }
}