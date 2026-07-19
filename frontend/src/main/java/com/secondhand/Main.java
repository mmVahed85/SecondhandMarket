package com.secondhand;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));

        Scene scene = new Scene(loader.load(), 500, 350);

        stage.setTitle("Second Hand Market");

        stage.setScene(scene);
        stage.setFullScreen(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}