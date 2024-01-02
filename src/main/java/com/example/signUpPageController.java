package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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

    public void checkSignUp() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\ASUS\\Documents\\VCS\\type-a-thon-edit\\src\\main\\java\\playersProfile.txt", true))) {
            writer.write(username.getText() + "," + password.getText() + ",0,0,0,0,0,0,0,0,0,0,0");
            writer.newLine();
            System.out.println("Sign up successful!");
    
            App m = new App();
            m.changeScene("afterLogin.fxml");
        }

        catch (IOException e) {
            System.err.println("Error writing to user credentials file.");
            e.printStackTrace();
        }
    }
    
    public void switchToLoginPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("loginPage.fxml");
    }
}
