package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class loginPageController 
{
    @FXML
    private Button loginButton;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button back;
    @FXML
    private Button signUpNow;

    private playerProfileController PlayerProfileController;

    public void setPlayerProfileController(playerProfileController PlayerProfileController) {
        this.PlayerProfileController = PlayerProfileController;
    }

    public void checkLogin(ActionEvent event) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\java\\playersProfile.txt"))) {
        String line;
        boolean userFound = false;

        while ((line = reader.readLine()) != null) {
            String[] credentials = line.split(",");
            if (credentials[0].equals(username.getText()) && credentials[1].equals(password.getText())) {
                userFound = true;
                wrongLogin.setText("Login Success!");

                loginButton.setText(username.getText());

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("afterLoginPage.fxml"));
                Parent root = loader.load();

                afterLoginPageController controller2 = loader.getController();
                controller2.displayUsername(username.getText());

                PlayerProfileController.getProfile(username.getText());

                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);  
                stage.setScene(scene);
                stage.show();

                break;
            }
        }

        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            wrongLogin.setText("Please enter your data!");
        } 
        else if (!userFound) {
            wrongLogin.setText("Wrong username or password!");
        }
        
        if(userFound)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = loader.load();

            MainPageController mainPageController = loader.getController();
            mainPageController.displayCurrentUser(username.getText());

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);  
            stage.setScene(scene);
            stage.show();
        }

    } catch (IOException e) {
        System.err.println("Error reading user credentials file.");
        e.printStackTrace();
    }
}


    public void switchToAfterLogin(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("afterLoginPage.fxml");
    }

    public void switchToMainPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("mainpage.fxml");
    }

    public void switchToSignUp(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("signUpPage.fxml");
    }
}
