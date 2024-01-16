package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


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

    @FXML
    private LineChart<String, Number> wpmGraph;

    @FXML
    private LineChart<String, Number> accuracyGraph;

    @FXML
    private CategoryAxis xaccuracy;

    @FXML
    private CategoryAxis xwpm;

    @FXML
    private NumberAxis yaccuracy;

    @FXML
    private NumberAxis ywpm;

    private List<String> currentWords;

    private int secondsRemaining;

    private int errorCount;

    private int totalChar;

    private String username;

    private BufferedWriter resultsWriter;

    private leaderboardController LeaderboardController;

    public void setLeaderboardController(leaderboardController LeaderboardController) {
        this.LeaderboardController = LeaderboardController;
    }

    public void setCurrentWords(List<String> words) {
        this.currentWords = words;
    }

    //USE THIS IN TYPE A THON
    public void initializeMyData(int secondsRemaining, int errorCount, int totalChar, int elapsedMins, int elapsedSecs) {

        System.out.println("Total Characters: " + totalChar);

        int totalWords = totalChar / 5;
        System.out.println("Total Words: " + totalWords);

        double wpm = ((double) totalWords) / (double) ((elapsedMins * 60) + elapsedSecs / 60.0);
        System.out.println("Initial WPM: " + wpm);

        System.out.println("initializeMyData called with: " +
            "secondsRemaining=" + secondsRemaining +
            ", errorCount=" + errorCount +
            ", totalChar=" + totalChar);
            
        // Assuming every word is 5 letters long
        double acc = (double) (totalChar - errorCount) / totalChar * 100;
    
        speed.setText(String.format("%.2f", wpm));
        accuracy.setText(String.format("%.1f", acc));
        timeSpent.setText(String.format("%d:%02d", secondsRemaining / 60, secondsRemaining % 60));
        Errors.setText(String.valueOf(errorCount));

        this.secondsRemaining = secondsRemaining;

        //Retrieve the username from userProfile.txt
        File directory = new File("src\\main\\java");
        File userProfile = new File("src\\main\\java\\com\\example\\userProfile.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(userProfile))) {
            username = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (File file : directory.listFiles()) {
            if(username != null){
                if (file.isFile() && file.getName().contains(username) && file.getName().endsWith("Profile.txt")) {
                    Path filePath = Paths.get("src\\main\\java\\" + username + "Profile.txt");
                    try { 
                        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            
                        String updatedLine = insertResults(lines.get(0), username, wpm, acc);
            
                        Files.write(filePath, Collections.singletonList(updatedLine), StandardCharsets.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private static String insertResults(String line, String username, double wpm, double acc) {
        int[] parts = convertToIntegerArray(line.split(","));
        int[] updatedParts = Arrays.copyOf(parts, parts.length);

        //parts[0] username, parts[1] password, parts[2] avg wpm, parts[3] avg acc, parts[4] best wpm, parts[5] best acc
        int avgWPM = parts[2]; int avgACC = parts[3];
        int totalWPM = 0, totalACC = 0;

        if(parts[24] != 0) { //If full, set the longest results to 0
                parts[24] = 0;
                parts[25] = 0;
            }

        for(int i = 24; i >= 8; i -= 2) { //Update 10 latest wpm and acc, odd is wpm, even is acc
            updatedParts[i] = parts[i - 2];
            updatedParts[i + 1] = parts[i - 1];
        }
        
        updatedParts[6] = (int) wpm; updatedParts[7] = (int) acc; //insert recent game wpm and acc

        for(int i = 6; i < updatedParts.length; i += 2) { //calculate average wpm and acc
            totalWPM += updatedParts[i];
            totalACC += updatedParts[i + 1];
        }

        int games = 0;
        for(int i = 6; i < updatedParts.length; i +=2) {
            if(updatedParts[i] != 0) {
                games++;
            }
        }

        avgWPM = (int) totalWPM / games ; avgACC = (int) totalACC / games ;
        updatedParts[2] = avgWPM; updatedParts[3] = avgACC;

        if(updatedParts[2] > updatedParts[4]) { //compare recent wpm and acc with best wpm and acc
            updatedParts[4] = updatedParts[2];
        }

        if(updatedParts[3] > updatedParts[5]) {
            updatedParts[5] = updatedParts[3];
        }

        return convertToString(updatedParts);
    }

    private static int[] convertToIntegerArray(String[] credentials) {
        int[] result = new int[credentials.length];
        for(int i = 0; i < credentials.length; i++) {
            result[i] = Integer.parseInt(credentials[i]);
        }

        return result;
    }

    private static String convertToString(int[] parts) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < parts.length; i++) {
            builder.append(parts[i]);
            if(i < parts.length - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public void playAgainRandom(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }

    @FXML
    void playAgainSame(ActionEvent event) {
        try {
            App a = new App();
            a.changeScene("mainpage.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = loader.load();

            // Access the controller of the MainPage.fxml
            MainPageController mainPageController = loader.getController();

            // Pass the current set of words to MainPageController
            mainPageController.setCurrentWords(currentWords);

            // Set the data without modifying the text area
            //mainPageController.initializeDataWithoutChangingUI(secondsRemaining, errorCount, totalChar);
            
            Stage stage = App.getStage(); // Use the static method to get the stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> wpmData = readDataFromFile("src\\main\\java\\com\\example\\words_per_minute.txt");
        List<String> accData = readDataFromFile("src\\main\\java\\com\\example\\accuracy.txt");

        // Initialize the LineChart with the read data
        XYChart.Series<String,Number> wpmseries = new XYChart.Series();
        wpmseries.setName("WPM");
        XYChart.Series<String,Number> accseries = new XYChart.Series();
        accseries.setName("Accuracy");

        for (int i = 0; i < wpmData.size(); i++) {
            // Assuming the x-axis represents time (1 second intervals)
            wpmseries.getData().add(new XYChart.Data(Integer.toString(i + 1), Integer.parseInt(wpmData.get(i))));
        }
        for (int i = 0; i < accData.size(); i++) {
            accseries.getData().add(new XYChart.Data(Integer.toString(i + 1), Integer.parseInt(accData.get(i))));
        }

        wpmGraph.getData().addAll(wpmseries);
        accuracyGraph.getData().addAll(accseries);

        for (XYChart.Series<String,Number> s : wpmGraph.getData()) {
            for (XYChart.Data<String,Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "WPM : " + d.getYValue()));

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }

        for (XYChart.Series<String,Number> s : accuracyGraph.getData()) {
            for (XYChart.Data<String,Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Accuracy : " + d.getYValue()));

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }
    }  
      
    private List<String> readDataFromFile(String fileName) {
        try {
            // Read all lines from the file
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return List.of(); // Return an empty list if there's an error reading the file
        }
    }
}