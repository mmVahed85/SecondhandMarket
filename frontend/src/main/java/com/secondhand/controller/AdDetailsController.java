package com.secondhand.controller;

import java.util.List;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.dto.ChatRoomResponse;
import com.secondhand.dto.CommentResponse;
import com.secondhand.dto.CreateCommentRequest;
import com.secondhand.dto.RatingRequest;
import com.secondhand.dto.RatingResponse;
import com.secondhand.service.AdApi;
import com.secondhand.service.ChatApi;
import com.secondhand.service.CommentApi;
import com.secondhand.service.RatingApi;
import com.secondhand.util.ApiResponse;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label priceLabel;
    @FXML private Label cityLabel;
    @FXML private Label categoryLabel;
    @FXML private Label descriptionLabel;
    @FXML private ImageView adImageView;
    @FXML private Label sellerNameLabel;
    @FXML private Label sellerRatingLabel;
    @FXML private Label ratingCount;
    @FXML private Label messageLabel;
    @FXML private VBox commentsBox;

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
        ratingCount.setText("تعداد نفرات امتیاز داده:" + ad.getRatingCount());

        if (ad.getImages() != null && !ad.getImages().isEmpty()) {

            try {

                String imageUrl = ad.getImages().get(0).getUrl();
                System.out.println(ad.getImages().size());
            System.out.println(ad.getImages().get(0).getUrl());

                adImageView.setImage(new Image(imageUrl, true));

            } catch (Exception e) {

                System.err.println("خطا در بارگذاری تصویر");
                e.printStackTrace();

            }

        }

        final int[] currentImage = {0};

        if (ad.getImages() != null && ad.getImages().size() > 1) {

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3), event -> {

                        currentImage[0]++;

                        if (currentImage[0] >= ad.getImages().size()) {
                            currentImage[0] = 0;
                        }

                        adImageView.setImage(
                                new Image(
                                        ad.getImages().get(currentImage[0]).getUrl(),
                                        true
                                )
                        );
                    })
            );

            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }

        loadComments();
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

        if(selectedRating == null){
            messageLabel.setText("لطفا امتیاز انتخاب کنید");
            return;
        }


        RatingRequest request = new RatingRequest();
        request.setScore(selectedRating);


        ApiResponse<RatingResponse> response =
                ratingApi.submitRating(
                        currentAd.getId(),
                        request
                );


        if(response.isSuccess()){
            messageLabel.setStyle("-fx-text-fill:green");
            messageLabel.setText(response.getMessage());
            sellerRatingLabel.setText("میانگین امتیازدهی: " + response.getData().getAverageScore());
            ratingCount.setText("تعداد نفرات امتیاز داده:" + response.getData().getRatingCount());
        }
        else{
            messageLabel.setStyle("-fx-text-fill:red");
            messageLabel.setText(response.getMessage());
        }

    }

    @FXML
    public void submitComment(ActionEvent event){

        String text = reviewCommentField.getText().trim();


        if(text.isEmpty()){
            messageLabel.setText("متن نظر خالی است");
            return;
        }


        CreateCommentRequest request =
                new CreateCommentRequest();

        request.setText(text);



        ApiResponse<CommentResponse> response =
                commentApi.createComment(
                        currentAd.getId(),
                        request
                );


        if(response.isSuccess()){

            messageLabel.setStyle("-fx-text-fill:green");
            messageLabel.setText(response.getMessage());

            reviewCommentField.clear();

            loadComments();

        }
        else{

            messageLabel.setStyle("-fx-text-fill:red");
            messageLabel.setText(response.getMessage());

        }

    }

    private void loadComments(){

        commentsBox.getChildren().clear();

        ApiResponse<List<CommentResponse>> response = commentApi.getComments(currentAd.getId());

        if(response.isSuccess()){

            for(CommentResponse c : response.getData()){

                Label label =
                        new Label(
                            c.getAuthor()
                            +" : "
                            +c.getText()
                        );


                label.setWrapText(true);

                commentsBox.getChildren().add(label);

            }

        }

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