package com.secondhand.controller;

import com.secondhand.dto.ChatRoomResponse;
import com.secondhand.service.ChatApi;
import com.secondhand.util.ApiResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChatListController {

    @FXML private VBox chatListContainer;

    // استفاده از API برای دریافت چت‌ها
    private final ChatApi chatApi = new ChatApi();

    @FXML
    public void initialize() {
        loadChatRooms();
    }

    private void loadChatRooms() {
        chatListContainer.getChildren().clear();

        // دریافت لیست چت‌ها از سرور
        ApiResponse<List<ChatRoomResponse>> response = chatApi.getChats();

        if (response.isSuccess() && response.getData() != null && !response.getData().isEmpty()) {
            for (ChatRoomResponse chatRoom : response.getData()) {
                HBox chatCard = createChatCard(chatRoom);
                chatListContainer.getChildren().add(chatCard);
            }
        } else {
            Label emptyLabel = new Label("شما هنوز هیچ گفت‌وگویی ندارید.");
            emptyLabel.setFont(new Font("B Yekan", 16));
            emptyLabel.setStyle("-fx-text-fill: #7f8c8d;");
            chatListContainer.getChildren().add(emptyLabel);
        }
    }

    private HBox createChatCard(ChatRoomResponse chatRoom) {
        HBox card = new HBox();
        card.setPadding(new Insets(15));
        card.setSpacing(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-cursor: hand;");

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-border-color: #dcdde1; -fx-border-radius: 10; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-cursor: hand;"));

        VBox infoBox = new VBox();
        infoBox.setSpacing(8);

        // دریافت نام کاربری مستقیم از بک‌اند
        String username = chatRoom.getOtherUser();

        // نمایش نام کاربری به عنوان متن اصلی
        Label nameLabel = new Label(username);
        nameLabel.setFont(new Font("B Yekan", 18));
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #19171cff;");

        // تگ نام کاربری
        Label usernameTag = new Label(username);
        usernameTag.setFont(new Font("Arial", 12));
        usernameTag.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #7f8c8d; -fx-padding: 3 8 3 8; -fx-background-radius: 12;");

        // قرار دادن نام کاربری و تگ در یک ردیف
        HBox nameAndTagBox = new HBox(10);
        nameAndTagBox.setAlignment(Pos.CENTER_LEFT);
        nameAndTagBox.getChildren().addAll(nameLabel, usernameTag);

        Label adTitleLabel = new Label("آگهی: " + chatRoom.getAdvertisementTitle());
        adTitleLabel.setFont(new Font("B Yekan", 14));
        adTitleLabel.setStyle("-fx-text-fill: #3498db;");

        Label lastMessageLabel = new Label(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage() : "پیامی ارسال نشده");
        lastMessageLabel.setFont(new Font("B Yekan", 14));
        lastMessageLabel.setStyle("-fx-text-fill: #7f8c8d;");

        infoBox.getChildren().addAll(nameAndTagBox, adTitleLabel, lastMessageLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        VBox timeBox = new VBox();
        timeBox.setAlignment(Pos.TOP_LEFT);

        Label timeLabel = new Label("");
        if (chatRoom.getLastMessageTime() != null) {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm - yyyy/MM/dd");
            timeLabel.setText(chatRoom.getLastMessageTime().format(formatter));
        }
        timeLabel.setFont(new Font("Arial", 12));
        timeLabel.setStyle("-fx-text-fill: #95a5a6;");

        timeBox.getChildren().add(timeLabel);

        card.getChildren().addAll(infoBox, timeBox);

        card.setOnMouseClicked(event -> openChatRoom(chatRoom, card));

        return card;
    }

    private void openChatRoom(ChatRoomResponse chatRoom, Node sourceNode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/chat.fxml"));
            Parent root = loader.load();

            // ارسال اطلاعات به کنترلر چت اصلی
            ChatController chatController = loader.getController();
            chatController.initData(chatRoom);

            Scene currentScene = sourceNode.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("خطا در باز کردن صفحه گفت‌وگو");
        }
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