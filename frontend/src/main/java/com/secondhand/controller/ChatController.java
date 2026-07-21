package com.secondhand.controller;

import com.secondhand.model.Message;
import com.secondhand.util.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ChatController {

    @FXML private Label chatPartnerNameLabel;
    @FXML private Label adTitleLabel;
    @FXML private VBox messagesContainer;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField messageInputField;

    private String sellerName;
    private String currentUser;

    // این متد از صفحه جزئیات صدا زده می‌شود تا اطلاعات فروشنده را به این صفحه پاس دهد
    public void initData(String partnerName, String adTitle) {
        this.sellerName = partnerName;
        this.chatPartnerNameLabel.setText("گفتگو با: " + partnerName);
        this.adTitleLabel.setText("درباره آگهی: " + adTitle);

        // پیدا کردن نام کسی که لاگین کرده
        this.currentUser = SessionManager.getLoggedInUsername();
        if (this.currentUser == null) {
            this.currentUser = "شما";
        }

        // یک پیام تستی از طرف فروشنده برای شروع مکالمه
        Message welcomeMsg = new Message(sellerName, currentUser, "سلام! آگهی من رو دیدید؟ سوالی دارید؟");
        addMessageBubble(welcomeMsg, false); // false یعنی پیام مال ما نیست (سمت چپ)
    }

    @FXML
    public void handleSendMessage(ActionEvent event) {
        String text = messageInputField.getText().trim();
        if (text.isEmpty()) {
            return;
        }

        // ساخت پیام جدید توسط خودمان
        Message myMsg = new Message(currentUser, sellerName, text);

        // اضافه کردن حباب پیام به صفحه (true یعنی پیام مال ماست و باید سمت راست باشد)
        addMessageBubble(myMsg, true);

        // پاک کردن کادر تایپ
        messageInputField.clear();
    }

    private void addMessageBubble(Message message, boolean isMine) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5));

        // تنظیم چینش: اگر پیام ماست راست‌چین، اگر نه چپ‌چین
        messageBox.setAlignment(isMine ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        VBox bubble = new VBox();
        bubble.setPadding(new Insets(10, 15, 10, 15));
        bubble.setSpacing(5);

        // رنگ‌بندی حباب چت (سبز برای من، سفید برای بقیه)
        if (isMine) {
            bubble.setStyle("-fx-background-color: #dcf8c6; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            bubble.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        // متن پیام
        Label textLabel = new Label(message.getText());
        textLabel.setFont(new Font("B Yekan", 16));
        textLabel.setStyle("-fx-text-fill: black;");
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(400);

        // زمان پیام
        Label timeLabel = new Label(message.getFormattedTime());
        timeLabel.setFont(new Font("Arial", 10));
        timeLabel.setStyle("-fx-text-fill: #888888;");
        timeLabel.setAlignment(Pos.BOTTOM_RIGHT);

        bubble.getChildren().addAll(textLabel, timeLabel);
        messageBox.getChildren().add(bubble);

        messagesContainer.getChildren().add(messageBox);

        // اسکرول خودکار به پایین‌ترین پیام
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}