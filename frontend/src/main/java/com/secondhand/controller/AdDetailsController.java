package com.secondhand.controller;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.ChatRoomResponse;
import com.secondhand.dto.CreateCommentRequest;
import com.secondhand.dto.RatingRequest;
import com.secondhand.dto.RatingResponse;
import com.secondhand.service.AdApi;
import com.secondhand.service.ChatApi;
import com.secondhand.service.CommentApi;
import com.secondhand.service.RatingApi;
import com.secondhand.util.ApiResponse;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label cityLabel;
    @FXML private Label categoryLabel;
    @FXML private Label descriptionLabel;
    @FXML private ImageView adImageView;
    @FXML private Label sellerNameLabel;
    @FXML private Label sellerRatingLabel;
    @FXML private Label messageLabel;

    // المان‌های جدید امتیاز و نظر
    @FXML private ComboBox<Integer> ratingComboBox;
    @FXML private TextField reviewCommentField;

    private AdvertisementResponse currentAd;
    private final ChatApi chatApi = new ChatApi();
    private final AdApi adApi = new AdApi();
    private final RatingApi ratingApi = new RatingApi();
    private final CommentApi commentApi = new CommentApi();

    @FXML
    public void initialize() {
        // پر کردن منوی کشویی با اعداد ۱ تا ۵
        ratingComboBox.getItems().addAll(1, 2, 3, 4, 5);
    }

    public void setAd(AdvertisementResponse ad) {
        this.currentAd = ad;

        titleLabel.setText(ad.getTitle());
        priceLabel.setText("قیمت: " + ad.getPrice() + " تومان");
        cityLabel.setText("شهر: " + ad.getCity());
        categoryLabel.setText("دسته‌بندی: " + (ad.getCategory() != null ? ad.getCategory().name() : "نامشخص"));
        descriptionLabel.setText(ad.getDescription() != null ? ad.getDescription() : "توضیحاتی ثبت نشده است.");
        sellerNameLabel.setText("فروشنده: " + (ad.getOwnerUsername() != null ? ad.getOwnerUsername() : "کاربر سامانه"));
        sellerRatingLabel.setText("میانگین امتیازدهی: " + ad.getAverageRating());

        if (ad.getImages() != null && !ad.getImages().isEmpty()) {

            try {

                adImageView.setImage(
                        new Image(
                                ad.getImages().get(0).getUrl(),
                                true
                        )
                );

            } catch (Exception e) {

                System.err.println("خطا در بارگذاری عکس آگهی");

            }

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

    @FXML
    public void addToFavorites(ActionEvent event) {
        ApiResponse<String> response = adApi.addFavorite(currentAd.getId());
        System.err.println(response.getMessage());
        if (response.isSuccess()) {
            if (currentAd != null) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText(response.getMessage());
            }
        }
        else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText(response.getMessage());
        }
    }

    @FXML
    public void submitRating(ActionEvent event) {

        Integer selectedRating = ratingComboBox.getValue();

        if (selectedRating == null) {

            messageLabel.setStyle("-fx-text-fill:red;");
            messageLabel.setText("لطفا امتیاز انتخاب کنید.");
            return;

        }

        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setScore(selectedRating);

        ApiResponse<RatingResponse> response =
                ratingApi.submitRating(currentAd.getId(), ratingRequest);

        if (!response.isSuccess()) {

            messageLabel.setStyle("-fx-text-fill:red;");
            messageLabel.setText(response.getMessage());
            return;

        }

        String comment = reviewCommentField.getText().trim();

        if (!comment.isEmpty()) {

            CreateCommentRequest commentRequest =
                    new CreateCommentRequest();

            commentRequest.setText(comment);

            commentApi.createComment(
                    currentAd.getId(),
                    commentRequest
            );
        }

        messageLabel.setStyle("-fx-text-fill:green;");
        messageLabel.setText("امتیاز و نظر ثبت شد.");

        ratingComboBox.setDisable(true);
        reviewCommentField.setDisable(true);
        ((Button) event.getSource()).setDisable(true);
    }

    @FXML
    public void startChat(ActionEvent event) {

        try {

            ApiResponse<ChatRoomResponse> response =
                    chatApi.createOrGetRoom(currentAd.getId());

            if (!response.isSuccess()) {

                messageLabel.setStyle("-fx-text-fill:red;");
                messageLabel.setText(response.getMessage());

                return;
            }

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/chat.fxml"));

            Parent root = loader.load();

            ChatController controller = loader.getController();

            controller.initData(response.getData());

            Scene scene =
                    ((Node) event.getSource()).getScene();

            scene.setRoot(root);

        }

        catch (Exception e) {

            e.printStackTrace();

        }

    }
}