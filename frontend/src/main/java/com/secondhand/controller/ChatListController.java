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
    private final ChatApi chatApi = new ChatApi();

    @FXML
    public void initialize() {
        loadChatRooms();
    }

    private void loadChatRooms() {
        chatListContainer.getChildren().clear();
        ApiResponse<List<ChatRoomResponse>> response = chatApi.getChats();

        if (response.isSuccess() && response.getData() != null && !response.getData().isEmpty()) {
            for (ChatRoomResponse chatRoom : response.getData()) {
                HBox chatCard = createChatCard(chatRoom);
                chatListContainer.getChildren().add(chatCard);
            }
        } else {
            Label emptyLabel = new Label("You have no conversations yet.");
            emptyLabel.setFont(new Font("Arial", 16));
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

        String firstName = chatRoom.getOtherUserFirstname();
        String lastName = chatRoom.getOtherUserLastname();
        String fullName = (firstName != null && lastName != null && !firstName.isEmpty() && !lastName.isEmpty())
                ? firstName + " " + lastName
                : chatRoom.getOtherUser();

        Label nameLabel = new Label(fullName);
        nameLabel.setFont(new Font("Arial", 18));
        nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #19171cff;");

        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.getChildren().add(nameLabel);

        Label adTitleLabel = new Label("Ad: " + chatRoom.getAdvertisementTitle());
        adTitleLabel.setFont(new Font("Arial", 14));
        adTitleLabel.setStyle("-fx-text-fill: #3498db;");

        Label lastMessageLabel = new Label(chatRoom.getLastMessage() != null ? chatRoom.getLastMessage() : "No messages sent");
        lastMessageLabel.setFont(new Font("Arial", 14));
        lastMessageLabel.setStyle("-fx-text-fill: #7f8c8d;");

        infoBox.getChildren().addAll(nameBox, adTitleLabel, lastMessageLabel);
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

            ChatController chatController = loader.getController();
            chatController.initData(chatRoom);

            Scene currentScene = sourceNode.getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error opening chat page");
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