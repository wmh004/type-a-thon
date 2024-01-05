package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

public class leaderboardController {

    private static final String DIRECTORY_PATH = "src\\main\\java";

    @FXML
    private Button back;
    @FXML
    private Label user1;
    @FXML
    private Label user2;
    @FXML
    private Label user3;
    @FXML
    private Label user4;
    @FXML
    private Label user5;
    @FXML
    private Label user6;
    @FXML
    private Label user7;
    @FXML
    private Label user8;
    @FXML
    private Label user9;
    @FXML
    private Label user10;
    @FXML
    private Label personalBestWPM;
    @FXML
    private Label personalBestAcc;
    @FXML
    private Label personalAverageWPM;
    @FXML
    private Label personalAverageAcc;

    public void initialize() {
        try {
            List<Player> players = readSignUpFiles();
            displayTopPlayers(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Player> readSignUpFiles() throws IOException {
        List<Player> players = new ArrayList<>();

        File directory = new File(DIRECTORY_PATH);

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith("Profile.txt")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length >= 6) {
                            String username = data[0];
                            int score = Integer.parseInt(data[2]) + Integer.parseInt(data[3]);
                            String avgWPM = data[2];
                            String avgACC = data[3];
                            String bestWPM = data[4];
                            String bestACC = data[5];
                            players.add(new Player(username, score, avgWPM, avgACC, bestWPM, bestACC));
                        }
                    }
                }
            }
        }

        return players;
    }

    private void displayTopPlayers(List<Player> players) {
        Collections.sort(players, Collections.reverseOrder()); // Sort players in descending order

        List<Label> labels = List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10);

        for (int i = 0; i < Math.min(players.size(), 10); i++) {
            Player currentPlayer = players.get(i);
            labels.get(i).setText(currentPlayer.toString());
        }
    }

    // Player class to store player data
    private static class Player implements Comparable<Player> {
        private final String username;
        private final int score;
        private final String avgWPM;
        private final String avgACC;
        private final String bestWPM;
        private final String bestACC;

        public Player(String username, int score, String avgWPM, String avgACC, String bestWPM, String bestACC) {
            this.username = username;
            this.score = score;
            this.avgWPM = avgWPM;
            this.avgACC = avgACC;
            this.bestWPM = bestWPM;
            this.bestACC = bestACC;
        }

        @Override
        public int compareTo(Player other) {
            return Integer.compare(this.score, other.score);
        }

        @Override
        public String toString() {
            return String.format("%-35s %-10s %-1s %-38s %-10s %-1s %-10s",
                    username, avgWPM, avgACC, "%", bestWPM, bestACC, "%");
        }
    }

    //Username from loginPageController
    private static String userProfile;

    public void getProfile(String username) {
        userProfile = username;
    }

    //Results from ResultController
    public void resultsToProfile(double wpm, double acc) {
        String filePath = "src\\main\\java\\playersProfile.txt";
        String username = userProfile;
        
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for(int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if(line.contains(username)) {
                    lines.set(i, insertResults(line, username, wpm, acc));
                    break;
                }
            }

            Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String insertResults(String line, String username, double wpm, double acc) {
        double[] parts = convertToDoubleArray(line.split(","));
        
        //parts[0] username, parts[1] password, parts[2] best wpm, parts[3] best acc, parts[4] avg wpm, parts[5] avg acc
        double bestWPM = parts[2]; double bestACC = parts[3];
        double avgWPM = 0, avgACC = 0, totalWPM = 0, totalACC = 0;

        if(parts[24] != 0) { //If full, set the longest results to 0
                parts[24] = 0;
                parts[25] = 0;
            }

        for(int i = 24; i >= 8; i -= 2) { //Update 10 latest wpm and acc
            parts[i] = parts[i - 2];
            parts[i + 1] = parts[i - 1];
        }

        parts[6] = wpm; parts[7] = acc;

        for(int i = 6; i < parts.length; i += 2) { //calculate average wpm and acc
            totalWPM += parts[i];
            totalACC += parts[i + 1];
        }

        avgWPM = totalWPM / 10.0; avgACC = totalACC / 10.0;
        parts[4] = avgWPM; parts[5] = avgACC;

        if(avgWPM > bestWPM) { //compare recent wpm and acc with best wpm and acc
            parts[2] = parts[4];
        }

        if(avgACC > bestACC) {
            parts[3] = parts[5];
        }

        return convertToString(parts);
    }

    private static double[] convertToDoubleArray(String[] credentials) {
        double[] result = new double[credentials.length];
        for(int i = 0; i < credentials.length; i++) {
            result[i] = Double.parseDouble(credentials[i]);
        }

        return result;
    }

    private static String convertToString(double[] parts) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < parts.length; i++) {
            builder.append(parts[i]);
            if(i < parts.length - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }
}
