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
                            String index4 = data[4];
                            String index5 = data[5];
                            String index6 = data[6];
                            players.add(new Player(username, score, index4, index5, index6));
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
        private final String index4;
        private final String index5;
        private final String index6;

        public Player(String username, int score, String index4, String index5, String index6) {
            this.username = username;
            this.score = score;
            this.index4 = index4;
            this.index5 = index5;
            this.index6 = index6;
        }

        @Override
        public int compareTo(Player other) {
            return Integer.compare(this.score, other.score);
        }

        @Override
        public String toString() {
            return String.format("%-35s %-10d %-1s %-40s %-10s %-1s %-10s",
                    username, score, index4, "%", index5, index6, "%");
        }
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }
}
