package com.secondhand.controller;

import com.secondhand.dto.LoginRequest;
import com.secondhand.dto.LoginResponse;
import com.secondhand.model.User;
import com.secondhand.service.AuthApi;
import com.secondhand.service.UserApi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private Label resultLabel;

    private final UserApi userApi = new UserApi();

    @FXML
    public void testBackend(){
        AuthApi authApi = new AuthApi();

        LoginRequest request = new LoginRequest("admin", "123456"); // مطمئن شوید این کاربر در دیتابیس هم‌گروهی شما وجود دارد

        try {
            LoginResponse response = authApi.login(request);

            // اگر لاگین موفق بود، توکن را ذخیره کن
            if (response.isSuccess() && response.getToken() != null) {
                com.secondhand.util.SessionManager.setToken(response.getToken());

                // حالا که توکن ذخیره شده، درخواست بعدی (GET) با موفقیت انجام می‌شود
                User user = userApi.getTestUser();
                resultLabel.setText("اتصال موفق!\nID : " + user.getId() + "\nUsername : " + user.getUsername());
            } else {
                resultLabel.setText("لاگین ناموفق: " + response.getMessage());
            }

        } catch (Exception e) {
            resultLabel.setText("خطا در ارتباط با سرور (کنسول را ببینید)");
            e.printStackTrace();
        }
    }

    // متد انتقال به صفحه ورود
    @FXML
    public void goToLogin(ActionEvent event) {
        try {
            // ۱. بارگذاری فایل گرافیکی صفحه ورود
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));

            // ۲. پیدا کردن صحنه (Scene) فعلی از روی دکمه کلیک شده
            Scene currentScene = ((Node) event.getSource()).getScene();

            // ۳. جایگزین کردن محتوای صحنه فعلی به جای ساخت صحنه جدید
            currentScene.setRoot(root);

        } catch (Exception e) {
            System.err.println("خطا در بارگذاری صفحه ورود");
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRegister(ActionEvent event) {
        try {
            // ۱. بارگذاری فایل گرافیکی صفحه ثبت‌نام
            Parent root = FXMLLoader.load(getClass().getResource("/view/register.fxml"));

            // ۲. پیدا کردن صحنه (Scene) فعلی
            Scene currentScene = ((Node) event.getSource()).getScene();

            // ۳. جایگزین کردن محتوای صحنه
            currentScene.setRoot(root);

        } catch (Exception e) {
            System.err.println("خطا در بارگذاری صفحه ثبت‌نام");
            e.printStackTrace();
        }
    }
}