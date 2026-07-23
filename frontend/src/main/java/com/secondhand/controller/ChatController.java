package com.secondhand.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.secondhand.dto.ChatRoomResponse;
import com.secondhand.dto.MessageResponse;
import com.secondhand.dto.SendMessageRequest;
import com.secondhand.model.Message;
import com.secondhand.service.ApiClient;
import com.secondhand.service.ChatApi;
import com.secondhand.util.ApiResponse;
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

    private final ChatApi chatApi = new ChatApi();

    @FXML private Label chatPartnerNameLabel;
    @FXML private Label adTitleLabel;
    @FXML private VBox messagesContainer;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField messageInputField;

    private ChatRoomResponse currentChat;

    public void initData(ChatRoomResponse chat) {
        currentChat = chat;

        String partnerFirstName = chat.getOtherUserFirstname();
        String partnerLastName = chat.getOtherUserLastname();
        String partnerFullName = (partnerFirstName != null && partnerLastName != null && !partnerFirstName.isEmpty() && !partnerLastName.isEmpty())
                ? partnerFirstName + " " + partnerLastName
                : chat.getOtherUser();

        chatPartnerNameLabel.setText("Chat with: " + partnerFullName);
        adTitleLabel.setText(chat.getAdvertisementTitle());

        loadMessages();
    }

    private void loadMessages() {
        ApiResponse<List<MessageResponse>> response = chatApi.getMessages(currentChat.getId());
        if (!response.isSuccess()) return;

        messagesContainer.getChildren().clear();
        for (MessageResponse msg : response.getData()) {
            boolean mine = msg.getSender().equals(SessionManager.getLoggedInUsername());
            addMessageBubble(msg, mine);
        }
    }

    @FXML
    public void handleSendMessage(ActionEvent event) {
        String text = messageInputField.getText().trim();
        if (text.isEmpty()) return;

        SendMessageRequest request = new SendMessageRequest();
        request.setText(text);

        ApiResponse<MessageResponse> response = chatApi.sendMessage(currentChat.getId(), request);

        if (response.isSuccess()) {
            addMessageBubble(response.getData(), true);
            messageInputField.clear();
        }
    }

    private void addMessageBubble(MessageResponse message, boolean isMine) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5));

        // --- تغییر جدید: پیام‌های شما سمت چپ، پیام‌های طرف مقابل سمت راست ---
        messageBox.setAlignment(isMine ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);

        VBox bubble = new VBox();
        bubble.setPadding(new Insets(10, 15, 10, 15));
        bubble.setSpacing(5);

        if (isMine) {
            bubble.setStyle("-fx-background-color: #dcf8c6; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        } else {
            bubble.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        }

        String senderFirstName = message.getSenderFirstname();
        String senderLastName = message.getSenderLastname();
        String senderFullName = (senderFirstName != null && senderLastName != null && !senderFirstName.isEmpty() && !senderLastName.isEmpty())
                ? senderFirstName + " " + senderLastName
                : message.getSender();

        String senderName = isMine ? "You" : senderFullName;
        Label senderLabel = new Label(senderName);
        senderLabel.setFont(new Font("Arial", 12));
        senderLabel.setStyle("-fx-text-fill: " + (isMine ? "#27ae60" : "#2980b9") + "; -fx-font-weight: bold;");

        Label textLabel = new Label(message.getText());
        textLabel.setFont(new Font("Arial", 16));
        textLabel.setStyle("-fx-text-fill: black;");
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(400);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Label timeLabel = new Label(message.getCreatedAt().format(formatter));
        timeLabel.setFont(new Font("Arial", 10));
        timeLabel.setStyle("-fx-text-fill: #888888;");
        // زمان رو هم متناسب با حباب‌ها تنظیم کردم
        timeLabel.setAlignment(isMine ? Pos.BOTTOM_LEFT : Pos.BOTTOM_RIGHT);

        bubble.getChildren().addAll(senderLabel, textLabel, timeLabel);
        messageBox.getChildren().add(bubble);
        messagesContainer.getChildren().add(messageBox);

        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/chat-list.fxml"));
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}