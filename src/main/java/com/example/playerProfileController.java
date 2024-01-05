package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

public class playerProfileController 
{
    @FXML
    private Button back;
    @FXML
    private Button next;
    

    public void switchtoLeaderboard(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("leaderboard.fxml");
    }

    public void switchToNextPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("playerProfile1.fxml");
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
            System.out.println("Error in updating player profile.");
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
}
