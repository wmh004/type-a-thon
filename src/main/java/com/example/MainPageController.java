package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    //loginPage
    @FXML
    private Button loginButton;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    Stage stage;

    public void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        loginPage m = new loginPage();

        //Example(will use file input & output)
        if(username.getText().toString().equals("22002402") && password.getText().toString().equals("12345678"))
        {
            wrongLogin.setText("Login Success!");

            m.changeScene("afterLogin.fxml");
        }

        else if(username.getText().isEmpty() && password.getText().isEmpty())
        {
            wrongLogin.setText("Please enter your data!");
        }

        else {
            wrongLogin.setText("Wrong username or password!");
        }
    }

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
