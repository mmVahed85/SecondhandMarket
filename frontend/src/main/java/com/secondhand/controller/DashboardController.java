package com.secondhand.controller;

import com.secondhand.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;

public class DashboardController {

    @FXML
    private TilePane adsContainer; // فضایی که آگهی‌ها در آن قرار می‌گیرند

    // این متد به صورت خودکار به محض باز شدن صفحه اجرا می‌شود
    @FXML
    public void initialize() {
        System.out.println("داشبورد با موفقیت باز شد!");
        // TODO: در مراحل بعدی، لیست آگهی‌ها را از بک‌اند می‌گیریم و اینجا رسم می‌کنیم.
    }

    // انتقال به صفحه ثبت آگهی
    @FXML
    public void goToCreateAd(ActionEvent event) {
        System.out.println("اینجا به صفحه ثبت آگهی منتقل می‌شویم...");
        // فعلا خالی می‌گذاریم تا صفحه‌اش را بسازیم
    }

    // خروج از حساب کاربری
    @FXML
    public void handleLogout(ActionEvent event) {
        // ۱. پاک کردن توکن از مموری
        SessionManager.logout();
        
        // ۲. برگشت به صفحه لاگین
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            System.err.println("خطا در خروج از حساب:");
            e.printStackTrace();
        }
    }
}