package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;

public class MainPageController implements Initializable{

    @FXML
    private Button exit;
    @FXML
    private BorderPane mainPage;
    @FXML
    private Button refresh;
    @FXML
    private Button login;

    private Stage stage;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
    }

    //loginPage
   /*  @FXML
    private Button loginButton;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    private Stage stage;
    private Scene scene;
    private Parent root;

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
    }*/

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
                e.printStackTrace();
            }
        } );
    }

    public void switchToLogin(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("loginPage.fxml");
    }
}
