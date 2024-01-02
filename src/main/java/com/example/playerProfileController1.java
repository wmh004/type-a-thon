package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class playerProfileController1 {
    @FXML
    private Button back;

    public void switchToPlayerProfile(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("playerProfile.fxml");
    }
}
