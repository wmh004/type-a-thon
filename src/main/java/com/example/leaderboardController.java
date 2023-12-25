package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class leaderboardController 
{
    @FXML
    private Button playerHistory;
    @FXML
    private Button back;

    public void switchToPlayerHistory(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("playerHistory.fxml");
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }
}
