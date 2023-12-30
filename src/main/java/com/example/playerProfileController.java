package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class playerProfileController 
{
    @FXML
    private Button back;

    public void switchtoLeaderboard(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("leaderboard.fxml");
    }
}
