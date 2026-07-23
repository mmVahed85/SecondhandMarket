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

        // وقتی موس روی کارت می‌رود کمی تغییر رنگ دهد
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10; -fx-border-color: #dcdde1; -fx-border-radius: 10; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-cursor: hand;"));

        // اطلاعات سمت راست کارت (نام شخص و عنوان آگهی)
        VBox infoBox = new VBox();
        infoBox.setSpacing(5);

        Label nameLabel = new Label(chatRoom.getOtherUser());
        nameLabel.setFont(new Font("B Yekan", 18));
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label adTitleLabel = new Label("آگهی: " + chatRoom.getAdvertisementTitle());
        adTitleLabel.setFont(new Font("B Yekan", 14));
        adTitleLabel.setStyle("-fx-text-fill: #3498db;");

        // آخرین پیام
        Label lastMessageLabel = new Label(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage() : "پیامی ارسال نشده");
        lastMessageLabel.setFont(new Font("B Yekan", 14));
        lastMessageLabel.setStyle("-fx-text-fill: #7f8c8d;");

        infoBox.getChildren().addAll(nameLabel, adTitleLabel, lastMessageLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS); // پر کردن فضای خالی

        // اطلاعات سمت چپ کارت (زمان آخرین پیام)
        VBox timeBox = new VBox();
        timeBox.setAlignment(Pos.TOP_LEFT);

        Label timeLabel = new Label("");
        if (chatRoom.getLastMessageTime() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - yyyy/MM/dd");
            timeLabel.setText(chatRoom.getLastMessageTime().format(formatter));
        }
        timeLabel.setFont(new Font("Arial", 12));
        timeLabel.setStyle("-fx-text-fill: #95a5a6;");

        timeBox.getChildren().add(timeLabel);

        card.getChildren().addAll(infoBox, timeBox);

        // رویداد کلیک روی کارت برای باز کردن صفحه چت اصلی
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