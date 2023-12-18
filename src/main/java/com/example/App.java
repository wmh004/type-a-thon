package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.geometry.Rectangle2D;

import java.io.IOException;



public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        stage.setTitle("Type-a-Thon");
        stage.getIcons().add(new Image("file:///C:/Users/maxwell/Coding%20Stuff/Java/typeathon/src/images/keyboard.png"));
        scene = new Scene(loadFXML("primary"), 1920, 1080);
        scene.getStylesheets().add(getClass().getResource("textbox.css").toExternalForm());
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}