package com.example;

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

public class leaderboardController 
{
    @FXML
    private Button back;

    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }
}
