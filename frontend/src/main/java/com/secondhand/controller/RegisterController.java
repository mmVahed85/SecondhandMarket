package com.secondhand.controller;

import com.secondhand.dto.RegisterRequest;
import com.secondhand.dto.RegisterResponse;
import com.secondhand.service.AuthApi;
import com.secondhand.util.ApiResponse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthApi authApi = new AuthApi();

    @FXML
    public void handleRegister(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {

            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Please fill in all fields.");
            return;
        }

        RegisterRequest request = new RegisterRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setUsername(username);
        request.setEmail(email);
        request.setPhone(phone);
        request.setPassword(password);

        ApiResponse<RegisterResponse> response = authApi.register(request);
        try {
            if(response.isSuccess()) {
                errorLabel.setStyle("-fx-text-fill: green;");
                errorLabel.setText(response.getMessage());
                System.out.println("Server Response: " + response);
            } else {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText(response.getMessage() != null ? response.getMessage() : "Invalid registration information.");
            }

        } catch (Exception e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText(response.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void goToLogin(ActionEvent event) {
        try {
            javafx.scene.Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            javafx.scene.Scene currentScene = ((javafx.scene.Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            System.err.println("Error loading login page:");
            e.printStackTrace();
        }
    }
}