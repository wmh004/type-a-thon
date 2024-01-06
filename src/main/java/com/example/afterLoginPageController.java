package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class afterLoginPageController
{
    @FXML
    private Button back;
    @FXML
    private Label usernameDisplay;

    public void displayUsername(String username) {
        usernameDisplay.setText(username);
    }
    
    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }
}

