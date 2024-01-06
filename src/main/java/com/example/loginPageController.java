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

    private BufferedWriter usernameWriter;

    public void checkLogin(ActionEvent event) throws IOException {
        File profilesDirectory = new File("src\\main\\");
        
        if (username.getText().trim().isEmpty() || password.getText().trim().isEmpty()) {
            wrongLogin.setText("Please enter your data!");
            return;
        }
    
        boolean userFound = false;
    
        for (File file : profilesDirectory.listFiles()) {
            if (file.isFile() && file.getName().endsWith("Profile.txt")) {
                // Check user credentials in each file
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        String[] credentials = line.split(",");
                        
                        if (credentials.length >= 2 && credentials[0].equals(username.getText()) && credentials[1].equals(password.getText())) {
                            // User found, perform login
                            userFound = true;
                            wrongLogin.setText("Login Success!");

                            try {
                                usernameWriter = new BufferedWriter(new FileWriter(new File("src\\main\\java\\com\\example\\userProfile.txt")));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            writeUsernameToFile(username.getText());
    
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("afterLoginPage.fxml"));
                            Parent root = loader.load();
    
                            afterLoginPageController controller2 = loader.getController();
                            controller2.displayUsername(username.getText());

                            Stage stage = (Stage) loginButton.getScene().getWindow();
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.show();
    
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading user credentials file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    
        if (!userFound) {
            wrongLogin.setText("Wrong username or password!");
        }
    }

    private void writeUsernameToFile(String username){
        try {
            // Write the username to the file
            usernameWriter.write(username);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (usernameWriter != null) {
                    usernameWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
