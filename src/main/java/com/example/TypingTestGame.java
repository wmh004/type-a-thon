package com.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TypingTestGame extends Application {
    private static final String TARGET_TEXT = "Hello, World!";
    private int currentIndex = 0;
    private TextArea textArea;  // Declare the TextArea variable as a class member

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        textArea = new TextArea();
        textArea.setEditable(true); // Allow manual insertion of text
        textArea.setText(TARGET_TEXT); // Initialize with the target text

        StackPane root = new StackPane();
        root.getChildren().add(textArea);

        Scene scene = new Scene(root, 300, 200);

        primaryStage.setTitle("Typing Test Game");
        primaryStage.setScene(scene);

        // Focus the TextArea to ensure it receives key events
        textArea.requestFocus();

        // Set the event handler for key events
        scene.setOnKeyTyped(this::handleKeyTyped);

        primaryStage.show();
    }

    private void handleKeyTyped(KeyEvent event) {
        char typedChar = event.getCharacter().charAt(0);
    
        if (currentIndex < TARGET_TEXT.length() && typedChar == TARGET_TEXT.charAt(currentIndex)) {
            // Correct key typed, change color to green
            textArea.appendText(String.valueOf(typedChar));
            currentIndex++;
    
            int start = currentIndex - 1;
            int end = currentIndex;
            textArea.setStyle(start + ", " + end + " { -fx-text-fill: green; }");
    
            if (currentIndex == TARGET_TEXT.length()) {
                // The user has typed the entire text
                System.out.println("Congratulations! You typed the text correctly.");
                // You can perform additional actions here, such as displaying a score or restarting the game.
            }
        } else {
            // Incorrect key typed, you can handle this case as needed
            System.out.println("Incorrect key typed.");
        }
    
        event.consume();
    }
    
}
