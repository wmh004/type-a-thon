package com.example;

import javafx.fxml.LoadException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class playerHistoryController 
{
    @FXML
    private Button back;

    public void switchtoLeaderboard(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("leaderboard.fxml");
    }
}
