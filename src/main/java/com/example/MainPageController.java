package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.application.Platform;

public class MainPageController {

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

    public void refresh(ActionEvent event) {
        stage = (Stage) mainPage.getScene().getWindow();
        stage.close();
        Platform.runLater( () -> {
            try {
                new App().start( new Stage() );
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } );
    }
}
