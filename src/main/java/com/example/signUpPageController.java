package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class signUpPageController 
{
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button signUp;
    @FXML
    private Button back;

    public void switchToLoginPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("loginPage.fxml");
    }
}
