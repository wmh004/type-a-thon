package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;

import java.io.IOException;

public class leaderboardController 
{
    @FXML
    private Button playerHistory;
    @FXML
    private Button back;
    @FXML
    private MenuButton gameMode;

    public void switchToPlayerProfile(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("playerProfile.fxml");
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }
}
