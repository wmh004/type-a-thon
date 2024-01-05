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

    public void checkSignUp() {
    String enteredUsername = username.getText().trim();
    String enteredPassword = password.getText().trim();

    if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
        wrongLogin.setText("Please enter both username and password");
    } else if (enteredUsername.length() != 8 || !enteredUsername.matches("\\d+")) {
        wrongLogin.setText("Enter a valid 8-digit numeric username");
    } else {
        String userFilename = "src\\main\\java" + enteredUsername + "Profile.txt";

        // Check if the username already exists
        if (usernameExists(userFilename)) {
            wrongLogin.setText("Username already registered");
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilename, true))) {
                writer.write(enteredUsername + "," + enteredPassword + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
                writer.newLine();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("afterLoginPage.fxml"));
                Parent root = loader.load();

                afterLoginPageController controller = loader.getController();
                controller.displayUsername(enteredUsername);

                Stage stage = (Stage) signUp.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Error writing to user credentials file.");
                e.printStackTrace();
            }
        }
    }
}

private boolean usernameExists(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length > 0 && parts[0].equals(username.getText().trim())) {
                return true; // Username exists
            }
        }
    } catch (IOException e) {
        System.err.println("Error checking username existence.");
        e.printStackTrace();
    }
    return false; // Username does not exist
}

    
    public void switchToLoginPage(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("loginPage.fxml");
    }
}
