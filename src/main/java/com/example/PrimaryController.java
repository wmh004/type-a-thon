package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    private Button exit;
    @FXML
    private BorderPane mainPage;
    @FXML
    private Button refresh;

    Stage stage;

    public void quit(ActionEvent event) {
        stage = (Stage) mainPage.getScene().getWindow();
        stage.close();
    }

}
