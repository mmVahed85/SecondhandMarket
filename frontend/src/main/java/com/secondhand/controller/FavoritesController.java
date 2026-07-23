package com.secondhand.controller;

import com.secondhand.dto.AdvertisementResponse;
import com.secondhand.model.*;
import com.secondhand.service.AdApi;
import com.secondhand.util.ApiResponse;
import com.secondhand.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.List;

public class FavoritesController {

    private final AdApi adApi = new AdApi();

    @FXML private TilePane favoritesContainer;

    @FXML
    public void initialize() {
        loadFavorites();
    }

    private void loadFavorites() {
        favoritesContainer.getChildren().clear();
        ApiResponse<List<AdvertisementResponse>> response = adApi.getMyFavorites();

        if(response.isSuccess()) {
            if (response.getData().isEmpty()) {
                Label noAdLabel = new Label("You haven't added any ads to your favorites yet.");
                noAdLabel.setFont(new Font("Arial", 18));
                favoritesContainer.getChildren().add(noAdLabel);
                return;
            }

            for (AdvertisementResponse ad : response.getData()) {
                VBox adCard = createAdCard(ad);
                favoritesContainer.getChildren().add(adCard);
            }
        } else {
            Label noAdLabel = new Label(response.getMessage());
            noAdLabel.setFont(new Font("Arial", 18));
            favoritesContainer.getChildren().add(noAdLabel);
        }
    }

    private VBox createAdCard(AdvertisementResponse ad) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefSize(250, 330);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        if (ad.getImages() != null && !ad.getImages().isEmpty()) {
            try {
                String imageUrl = ad.getImages().get(0).getUrl();
                imageView.setImage(new Image(imageUrl, true));
            } catch (Exception ignored) {}
        }

        Label titleLabel = new Label(ad.getTitle());
        titleLabel.setFont(new Font("Arial", 16));
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label priceLabel = new Label("Price: " + ad.getPrice() + " Tomans");
        priceLabel.setFont(new Font("Arial", 14));
        priceLabel.setStyle("-fx-text-fill: #4CAF50;");

        Button detailsButton = new Button("View Details");
        detailsButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        detailsButton.setMaxWidth(Double.MAX_VALUE);
        detailsButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ad-details.fxml"));
                Parent root = loader.load();
                AdDetailsController detailsController = loader.getController();
                detailsController.setAd(ad);
                Scene currentScene = ((Node) e.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button removeButton = new Button("Remove from Favorites");
        removeButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        removeButton.setMaxWidth(Double.MAX_VALUE);
        removeButton.setOnAction(e -> {
            ApiResponse<AdvertisementResponse> response = adApi.deleteFavorite(ad.getId());
            if(response.isSuccess()) {
                loadFavorites();
            } else {
                System.out.println(response.getMessage());
            }
        });

        card.getChildren().addAll(imageView, titleLabel, priceLabel, detailsButton, removeButton);
        return card;
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