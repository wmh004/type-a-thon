package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class ResultController implements Initializable {

    @FXML Label speed; //USE LABELS INSTEAD OF TEXTFIELD BECAUSE LABELS ARE READ ONLY TEXT

    @FXML
    private Label accuracy;

    @FXML
    private Label timeSpent;

    @FXML
    private Label Errors;

    @FXML
    private Button playRandom;

    @FXML
    private Button playSame;

    private List<String> currentWords;

    private int secondsRemaining;

    private int errorCount;

    private int totalChar;

    public void setCurrentWords(List<String> words) {
        this.currentWords = words;
    }

    //USE THIS IN TYPE A THON
    public void initializeMyData(int secondsRemaining, int errorCount, int totalChar) {

        System.out.println("Total Characters: " + totalChar);

        int totalWords = totalChar / 5;
        System.out.println("Total Words: " + totalWords);

        double wpm = ((double) totalWords) / ((60.0 - secondsRemaining) / 60.0);
        System.out.println("Initial WPM: " + wpm);

        System.out.println("initializeMyData called with: " +
            "secondsRemaining=" + secondsRemaining +
            ", errorCount=" + errorCount +
            ", totalChar=" + totalChar);
            
        // Assuming every word is 5 letters long
        double acc = (double) (totalWords - errorCount) / totalWords * 100;
    
        speed.setText(String.format("%.2f", wpm));
        accuracy.setText(String.format("%.1f", acc));
        timeSpent.setText(String.format("%d:%02d", secondsRemaining / 60, secondsRemaining % 60));
        Errors.setText(String.valueOf(errorCount));

        this.secondsRemaining = secondsRemaining;
    }
    
    

    @FXML
    void playAgainRandom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void playAgainSame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = loader.load();

            // Access the controller of the MainPage.fxml
            MainPageController mainPageController = loader.getController();

            // Pass the current set of words to MainPageController
            mainPageController.setCurrentWords(currentWords);

            // Set the data without modifying the text area
            mainPageController.initializeDataWithoutChangingUI(secondsRemaining, errorCount, totalChar);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the initial set of words when the controller is created
        
    }    
    
}