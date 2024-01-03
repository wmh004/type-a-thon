package com.example;

// App.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("mainpage.fxml")); //this line is just the same as the first 2 lines
            Parent root = FXMLLoader.load(getClass().getResource("mainpage.fxml"));
            //The initialize() method of the GameController will be automatically called when the MainPage.fxml is loaded via the FXMLLoader
            primaryStage.setTitle("Type a thon");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception appropriately in a real application
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
