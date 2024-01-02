package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

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
}
