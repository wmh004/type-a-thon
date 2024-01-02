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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.io.*;
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
    @FXML
    private Label wrongLogin;

    public void checkSignUp() 
    {
        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) 
        {
            wrongLogin.setText("Please enter your data!");
        }
        else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\ASUS\\Documents\\VCS\\type-a-thon-edit\\src\\main\\java\\playersProfile.txt", true))) {
                writer.write(username.getText() + "," + password.getText() + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
                writer.newLine();
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("afterLoginPage.fxml"));
                    Parent root = loader.load();

                    afterLoginPageController controller = loader.getController();
                    controller.displayUsername(username.getText());

                    Stage stage = (Stage) signUp.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                
            }

            catch (IOException e) {
                System.err.println("Error writing to user credentials file.");
                e.printStackTrace();
            }
        }
    }
    
    public void switchToLoginPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("loginPage.fxml");
    }
}
