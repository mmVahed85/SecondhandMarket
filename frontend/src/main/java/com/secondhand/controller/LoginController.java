package com.secondhand.controller;

import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.service.AuthApi;
import com.secondhand.util.ApiResponse;

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        // روند عادی برای سایر کاربران (درخواست به سرور)
        LoginRequest request = new LoginRequest(username, password);

        try {
            ApiResponse<LoginResponse> response = authApi.login(request);

            if (response.isSuccess() && response.getData().getToken() != null) {
                com.secondhand.util.SessionManager.setToken(response.getData().getToken());
                com.secondhand.util.SessionManager.login(username);
                com.secondhand.util.SessionManager.setRole(response.getData().getRole());

                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText("Login successful! Redirecting...");

                try {
                    javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
                    javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
                    currentScene.setRoot(root);
                } catch (Exception ex) {
                    System.err.println("Error loading dashboard:");
                    ex.printStackTrace();
                }

            } else {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(response.getMessage() != null ? response.getMessage() : "Invalid login credentials.");
            }
        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Error connecting to the server!");
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
            System.err.println("Error loading registration page:");
            e.printStackTrace();
        }
    }
}