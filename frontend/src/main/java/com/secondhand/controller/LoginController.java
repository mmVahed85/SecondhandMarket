package com.secondhand.controller;

import com.secondhand.model.LoginRequest;
import com.secondhand.model.LoginResponse;
import com.secondhand.service.AuthApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthApi authApi = new AuthApi();

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("لطفاً نام کاربری و رمز عبور را وارد کنید.");
            return;
        }

        // --- بخش راه مخفی ورود ادمین ---
        if (username.equalsIgnoreCase("admin") && password.equals("admin")) {
            com.secondhand.util.SessionManager.login("admin");
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("ورود مدیر سیستم! در حال انتقال...");

            try {
                javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/admin-panel.fxml"));
                javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        // -----------------------------------------------------------

        LoginRequest request = new LoginRequest(username, password);

        try {
            // در ساختار جدید، اگر رمز اشتباه باشد AuthApi مستقیماً Exception پرتاب می‌کند
            LoginResponse response = authApi.login(request);

            // اگر به این خط رسیدیم، یعنی لاگین صددرصد موفق بوده است
            if (response != null && response.getToken() != null) {
                com.secondhand.util.SessionManager.setToken(response.getToken());
                com.secondhand.util.SessionManager.login(username);

                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("ورود موفق! در حال انتقال...");

                try {
                    javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
                    javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
                    currentScene.setRoot(root);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (RuntimeException e) {
            // پیام ارسالی از بک‌اند را مستقیماً اینجا می‌خوانیم و نمایش می‌دهیم
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("خطا در ارتباط با سرور!");
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRegister(javafx.event.ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/register.fxml"));
            javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            System.err.println("خطا در بارگذاری صفحه ثبت‌نام:");
            e.printStackTrace();
        }
    }
}