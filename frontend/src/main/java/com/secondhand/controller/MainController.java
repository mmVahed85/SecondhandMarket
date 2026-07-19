package com.secondhand.controller;

import com.secondhand.model.LoginRequest;
import com.secondhand.model.LoginResponse;
import com.secondhand.model.User;
import com.secondhand.service.AuthApi;
import com.secondhand.service.UserApi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label resultLabel;

    private final UserApi userApi = new UserApi();
    

    @FXML
    public void testBackend(){
        AuthApi authApi = new AuthApi();

        LoginRequest request =
                new LoginRequest(
                        "admin",
                        "123456"
                );

        LoginResponse response =
                authApi.login(request);

        resultLabel.setText(response.getMessage());

        User user = userApi.getTestUser();
        resultLabel.setText("ID : " + user.getId() + "\nUsername : " + user.getUsername());
    }

}