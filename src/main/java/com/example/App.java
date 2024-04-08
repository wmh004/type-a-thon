package com.example;

import java.io.IOException;

// App.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    private static Stage stg;
    private static Scene scene;
    
    public static Stage getStage() {
        return stg;
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        stage.setResizable(true);
        Parent root = FXMLLoader.load(getClass().getResource("mainpage.fxml"));
        stage.setTitle("Type");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/keyboard.png")));
        scene = new Scene(root, 1600, 900);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}   