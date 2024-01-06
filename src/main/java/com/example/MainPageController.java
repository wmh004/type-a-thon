package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Button resultButton;

    @FXML
    private TextArea textDisplay;

    @FXML
    private MenuItem numbersButton;

    @FXML
    private MenuItem punctuationButton;

    @FXML
    private ChoiceBox<Integer> timerChoiceBox;

    @FXML
    private ChoiceBox<Integer> wordChoiceBox;

    @FXML
    private Label elapsedTimeLabel;

     @FXML
    private Label displayCurrentUser;

    @FXML
    private Button quotesButton;

    @FXML
    private Button wordButton;

    @FXML
    private Button SDbutton;

    @FXML
    private Button exit;

    @FXML
    private BorderPane mainPage;

    @FXML
    private Button refresh;

    @FXML
    private Button login;

    @FXML
    private Button leaderboard;

    @FXML
    private MenuItem quotesMode;

    private Stage stage;

    public int errorCount;

    public int totalChar;

    public int secondsRemaining;

    private char expectedKey;

    private char typedKey;

    private List<String> enteredWords;

    private List<String> currentWords;

    private BufferedWriter wpmWriter;

    private BufferedWriter accuracyWriter;


    List<String> quotesListFiles = Arrays.asList("src\\main\\java\\com\\example\\quotesList.txt", "src\\main\\java\\com\\example\\quotes2List.txt", "src\\main\\java\\com\\example\\quotes3List.txt","src\\main\\java\\com\\example\\quotes4List.txt","src\\main\\java\\com\\example\\quotes5List.txt","src\\main\\java\\com\\example\\quotes6List.txt");
    List<String> quotesList = new ArrayList<>();
    

    private int elapsedMins = 0;
    private int elapsedSecs = 0;

    int indexOfLine = 0;

    //set to an empty string as a placeholder, and its value is replaced when the actual words are loaded from the file
    //An empty string is a string with zero characters. It's different from a null value or a string with a space character.
    String arr = ""; 

    // Timer variables
    Timeline timeline;

    int mins = 1, secs = 0; // Set the initial time to 01:00

    boolean timerStarted = false;

    public MainPageController() {
        try {
            wpmWriter = new BufferedWriter(new FileWriter(new File("src\\main\\java\\com\\example\\words_per_minute.txt")));
            accuracyWriter = new BufferedWriter(new FileWriter(new File("src\\main\\java\\com\\example\\accuracy.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadTest() {
        int wordsToDisplay = wordChoiceBox.getValue();

        if (wordsToDisplay == 10 || wordsToDisplay == 25 || wordsToDisplay == 50 || wordsToDisplay == 100) {
            timeLabel.setVisible(false); // Hide timelabel for specific word counts
            elapsedTimeLabel.setVisible(true);
            
        } else {
            timeLabel.setVisible(true); // Show timelabel for other word counts
            elapsedTimeLabel.setVisible(false);
            
        }

        List<String> wordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("wordsList.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Shuffle the wordList to create a random arrangement
        Collections.shuffle(wordList);

        wordList = wordList.subList(0, Math.min(wordList.size(), wordsToDisplay));

        // Concatenate the selected words to form the text
        arr = String.join(" ", wordList);

        textDisplay.setText(arr);

        textDisplay.requestFocus();
        errorCount = 0;
        indexOfLine = 0;
        totalChar = 0;

        textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
        textDisplay.selectRange(indexOfLine, indexOfLine + 1); // highlighting the first character

        // Declare a final array to hold the boolean value
        textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
            private boolean withinWord = false;
            private boolean spacePressed = false;

            @Override
            public void handle(KeyEvent event) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }

                if (event.getCharacter().equals(" ")) {
                    // Handle space character
                    withinWord = false; // Mark current word as completed
                    spacePressed = true; // Mark space as pressed
                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                    indexOfLine++;
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                } else if (event.getCharacter().equals("\b")) {
                    // Ignore backspace when space is pressed and word is completed
                    if (!spacePressed && indexOfLine > 0) {
                        // Only allow backspace within a word
                        indexOfLine--;
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }
                } else {
                    // Update the character count when a non-space character is typed
                    totalChar++;

                    if (indexOfLine < arr.length()) {
                        expectedKey = arr.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);
            
                        if (typedKey != expectedKey) {
                            errorCount++;
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
                        } else {
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                        }
            
                        indexOfLine++;
                        withinWord = true; // Mark that we are still within a word
                        spacePressed = false; // Reset the spacePressed flag
            
                        // Update the elapsed time label
                        updateElapsedTimeLabel();
                    }
            
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
            
                    if (indexOfLine == arr.length()) {
                        stopTimer();
                        withinWord = false; // Mark the current word as completed
                        spacePressed = false; // Reset the spacePressed flag
                        switchSceneToResult();
                    }
                }
            }
        });
    textDisplay.setEditable(false);
    enteredWords = List.of(textDisplay.getText().split("\\s+"));
    }

    @FXML
    void replaceTextWithPunctuation() {
        int wordsToDisplay = wordChoiceBox.getValue();

        MixFiles mixFiles = new MixFiles();
        mixFiles.mixFiles("src\\main\\java\\com\\example\\wordsList.txt", "src\\main\\java\\com\\example\\PunctuationList.txt", "mixedText.txt");


        List<String> mixedText = MixFiles.readFile("mixedText.txt");

        List<String> wordList = mixedText.subList(0, Math.min(mixedText.size(), wordsToDisplay));
        
        String mixedTextString = String.join(" ", wordList);
        
        textDisplay.setText(mixedTextString);

        // Reset the state as needed
        errorCount = 0;
        indexOfLine = 0;
        totalChar = 0;
        textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
        textDisplay.selectRange(indexOfLine, indexOfLine + 1); //highlighting the first character 
            textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
            private boolean withinWord = false;
            private boolean spacePressed = false;

            @Override
            public void handle(KeyEvent event) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }

                if (event.getCharacter().equals(" ")) {
                    // Handle space character
                    withinWord = false; // Mark current word as completed
                    spacePressed = true; // Mark space as pressed
                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                    indexOfLine++;
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                } else if (event.getCharacter().equals("\b")) {
                    // Ignore backspace when space is pressed and word is completed
                    if (!spacePressed && indexOfLine > 0) {
                        // Only allow backspace within a word
                        indexOfLine--;
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }
                } else {
                    // Update the character count when a non-space character is typed
                    totalChar++;

                    if (indexOfLine < mixedTextString.length()) {
                        expectedKey = mixedTextString.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);

                        if (typedKey != expectedKey) {
                            errorCount++;
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
                        } else {
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                        }

                        indexOfLine++;
                        withinWord = true; // Mark that we are still within a word
                        spacePressed = false; // Reset the spacePressed flag
                    }

                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);

                    if (indexOfLine == mixedTextString.length()) {
                        stopTimer();
                        withinWord = false; // Mark the current word as completed
                        spacePressed = false; // Reset the spacePressed flag
                        switchSceneToResult();
                    }
                }
            }
        });
    
        textDisplay.setEditable(false);
        enteredWords = List.of(textDisplay.getText().split("\\s+"));
    }

    @FXML
    void replaceTextWithNumbers() {
        int wordsToDisplay = wordChoiceBox.getValue();

        MixFiles2 mixFiles = new MixFiles2();
        mixFiles.mixFiles("src\\main\\java\\com\\example\\wordsList.txt", "src\\main\\java\\com\\example\\NumbersList.txt", "mixedText2.txt");


        List<String> mixedText = MixFiles.readFile("mixedText2.txt");

        List<String> wordList = mixedText.subList(0, Math.min(mixedText.size(), wordsToDisplay));

        String mixedTextString = String.join(" ", wordList);
        textDisplay.setText(mixedTextString);

        // Reset the state as needed
        errorCount = 0;
        indexOfLine = 0;
        totalChar = 0;
        textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
        textDisplay.selectRange(indexOfLine, indexOfLine + 1); //highlighting the first character 
            textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
            private boolean withinWord = false;
            private boolean spacePressed = false;

            @Override
            public void handle(KeyEvent event) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }

                if (event.getCharacter().equals(" ")) {
                    // Handle space character
                    withinWord = false; // Mark current word as completed
                    spacePressed = true; // Mark space as pressed
                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                    indexOfLine++;
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                } else if (event.getCharacter().equals("\b")) {
                    // Ignore backspace when space is pressed and word is completed
                    if (!spacePressed && indexOfLine > 0) {
                        // Only allow backspace within a word
                        indexOfLine--;
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }
                } else {
                    // Update the character count when a non-space character is typed
                    totalChar++;

                    if (indexOfLine < mixedTextString.length()) {
                        expectedKey = mixedTextString.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);

                        if (typedKey != expectedKey) {
                            errorCount++;
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
                        } else {
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                        }

                        indexOfLine++;
                        withinWord = true; // Mark that we are still within a word
                        spacePressed = false; // Reset the spacePressed flag
                    }

                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);

                    if (indexOfLine == mixedTextString.length()) {
                        stopTimer();
                        withinWord = false; // Mark the current word as completed
                        spacePressed = false; // Reset the spacePressed flag
                        switchSceneToResult();
                    }
                }
            }
        });

                textDisplay.setEditable(false); 
                enteredWords = List.of(textDisplay.getText().split("\\s+"));
    }

    @FXML
    void replaceTextWithQuotes() {
        String quotesListFilePath = getRandomQuotesListFilePath();
        quotesList = readFile(quotesListFilePath);

        if (quotesList.isEmpty()) {
            System.out.println("Quotes list is empty.");
            // Handle the case where the quotes list is empty (log, show a message, etc.)
            return;
        }

        String selectedQuote = String.join(" ", quotesList);
        // Add a message based on the selected quote file
        String message = getMessageForQuoteFile(quotesListFilePath);
        if (message != null && !message.isEmpty()) {
            showAlert((Stage) textDisplay.getScene().getWindow(), "Quote Information", message);
        }
        textDisplay.setText(selectedQuote);

        // Reset the state as needed
        errorCount = 0;
        indexOfLine = 0;
        totalChar = 0;
        textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
        textDisplay.selectRange(indexOfLine, indexOfLine + 1); // highlighting the first character

        textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
            private boolean withinWord = false;
            private boolean spacePressed = false;

            @Override
            public void handle(KeyEvent event) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }

                if (event.getCharacter().equals(" ")) {
                    // Handle space character
                    withinWord = false; // Mark current word as completed
                    spacePressed = true; // Mark space as pressed
                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                    indexOfLine++;
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                } else if (event.getCharacter().equals("\b")) {
                    // Ignore backspace when space is pressed and word is completed
                    if (!spacePressed && indexOfLine > 0) {
                        // Only allow backspace within a word
                        indexOfLine--;
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }
                } else {
                    // Update the character count when a non-space character is typed
                    totalChar++;

                    if (indexOfLine < selectedQuote.length()) {
                        expectedKey = selectedQuote.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);

                        if (typedKey != expectedKey) {
                            errorCount++;
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
                        } else {
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                        }

                        indexOfLine++;
                        withinWord = true; // Mark that we are still within a word
                        spacePressed = false; // Reset the spacePressed flag
                    }

                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);

                    if (indexOfLine == selectedQuote.length()) {
                        stopTimer();
                        withinWord = false; // Mark the current word as completed
                        spacePressed = false; // Reset the spacePressed flag
                        switchSceneToResult();
                    }
                }
            }
        });

        textDisplay.setEditable(false);
        enteredWords = List.of(textDisplay.getText().split("\\s+"));
    }

    private String getMessageForQuoteFile(String quoteFilePath) {
        switch (quoteFilePath) {
            case "src\\main\\java\\com\\example\\quotesList.txt":
                return "This quote is from Martin Luther King.";
            case "src\\main\\java\\com\\example\\quotes2List.txt":
                return "This quote is from Erwin(AOT).";
            case "src\\main\\java\\com\\example\\quotes3List.txt":
                return "This quote is from Kim Carnby(Sweet home).";
            case "src\\main\\java\\com\\example\\quotes4List.txt":
                return "This quote is from Abraham Lincoln";
            case "src\\main\\java\\com\\example\\quotes5List.txt":
                return "This quote is from Lee Sookyung(ORV)";
            case "src\\main\\java\\com\\example\\quotes6List.txt":
                return "This quote is from Han Sooyoung(ORV)";
            default:
                return null;
        }
    }
    
    private void showAlert(Stage ownerStage, String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
    
        // Set the owner stage to make the alert appear on top of it
        alert.initOwner(ownerStage);
    
        alert.showAndWait();
    }
    

    @FXML
    void SUDDENDEATHMODE() {
        int wordsToDisplay = wordChoiceBox.getValue();

        List<String> wordList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("wordsList.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Shuffle the wordList to create a random arrangement
        Collections.shuffle(wordList);

        wordList = wordList.subList(0, Math.min(wordList.size(), wordsToDisplay));

        // Concatenate the selected words to form the text
        arr = String.join(" ", wordList);

        textDisplay.setText(arr);

        textDisplay.requestFocus();
        errorCount = 0;
        indexOfLine = 0;
        totalChar = 0;

        textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
        textDisplay.selectRange(indexOfLine, indexOfLine + 1); // highlighting the first character

        // Declare a final array to hold the boolean value
        textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
            private boolean withinWord = false;
            private boolean spacePressed = false;

            @Override
            public void handle(KeyEvent event) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }

                if (event.getCharacter().equals(" ")) {
                    // Handle space character
                    withinWord = false; // Mark current word as completed
                    spacePressed = true; // Mark space as pressed
                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                    indexOfLine++;
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                } else if (event.getCharacter().equals("\b")) {
                    // Ignore backspace when space is pressed and word is completed
                    if (!spacePressed && indexOfLine > 0) {
                        // Only allow backspace within a word
                        indexOfLine--;
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }
                } else {
                    // Update the character count when a non-space character is typed
                    totalChar++;

                    if (indexOfLine < arr.length()) {
                        expectedKey = arr.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);

                        if (typedKey != expectedKey) {
                            errorCount++;
                            stopTimer(); // Stop the timer on the first mistake
                            switchSceneToResult();
                        } else {
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                        }

                        indexOfLine++;
                        withinWord = true; // Mark that we are still within a word
                        spacePressed = false; // Reset the spacePressed flag
                    }

                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);

                    if (indexOfLine == arr.length()) {
                        stopTimer();
                        withinWord = false; // Mark the current word as completed
                        spacePressed = false; // Reset the spacePressed flag
                        switchSceneToResult();
                    }
                }
            }
        });
    textDisplay.setEditable(false);
    enteredWords = List.of(textDisplay.getText().split("\\s+"));
}

    private String getRandomQuotesListFilePath() {
        if (quotesListFiles.isEmpty()) {
            throw new IllegalArgumentException("quotesListFiles is empty");
        }
    
        Random random = new Random();
        int randomIndex = random.nextInt(quotesListFiles.size());
        return quotesListFiles.get(randomIndex);
    }

    public List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    //buang nanti
    void handleBackspace(){
        if(indexOfLine > 0){
            indexOfLine--;
            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3");
            textDisplay.selectRange(indexOfLine, indexOfLine + 1);
        }
    }

    void change() {
        if (timerStarted) {
            if (secs == 0) {
                if (mins == 0) {
                    stopTimer(); // Stop the timer when the countdown is complete
                    textDisplay.setEditable(false);
                    textDisplay.setFocusTraversable(false);
                    textDisplay.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                } else {
                    mins--;
                    secs = 59;
                }
            } else {
                secs--;
    
                // Update secondsRemaining
                secondsRemaining = mins * 60 + secs;
    
                updateTimeLabel();
    
                // Update elapsed time
                updateElapsedTime();
    
                // Check if the timer has reached 0
                if (secs <= 0 && mins <= 0) {
                    stopTimer(); // Stop the timer
                    switchSceneToResult(); // Switch to results scene
                    secondsRemaining = 0; // Ensure secondsRemaining is set to 0
                }
            }
        }
    }
    

    public void setEnteredWords(List<String> enteredWords) {
        this.enteredWords = enteredWords;
    }

    public void setCurrentWords(List<String> words) {
        if (words != null) {
            this.currentWords = words;
    
            if (textDisplay != null) {
                String wordsText = String.join(" ", words);
                textDisplay.setText(wordsText);
    
                errorCount = 0;
                indexOfLine = 0;
                totalChar = 0;
                textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                textDisplay.selectRange(indexOfLine, indexOfLine + 1); // highlighting the first character
    
                textDisplay.setOnKeyTyped(new EventHandler<KeyEvent>() {
            private boolean withinWord = false;
            private boolean spacePressed = false;

            @Override
            public void handle(KeyEvent event) {
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }

                if (event.getCharacter().equals(" ")) {
                    // Handle space character
                    withinWord = false; // Mark current word as completed
                    spacePressed = true; // Mark space as pressed
                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                    indexOfLine++;
                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                } else if (event.getCharacter().equals("\b")) {
                    // Ignore backspace when space is pressed and word is completed
                    if (!spacePressed && indexOfLine > 0) {
                        // Only allow backspace within a word
                        indexOfLine--;
                        textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                    }
                } else {
                    // Update the character count when a non-space character is typed
                    totalChar++;

                    if (indexOfLine < wordsText.length()) {
                        expectedKey = wordsText.charAt(indexOfLine);
                        typedKey = event.getCharacter().charAt(0);

                        if (typedKey != expectedKey) {
                            errorCount++;
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
                        } else {
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                        }

                        indexOfLine++;
                        withinWord = true; // Mark that we are still within a word
                        spacePressed = false; // Reset the spacePressed flag
                    }

                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);

                    if (indexOfLine == wordsText.length()) {
                        stopTimer();
                        withinWord = false; // Mark the current word as completed
                        spacePressed = false; // Reset the spacePressed flag
                        switchSceneToResult();
                    }
                }
            }
        });
    
                textDisplay.setEditable(false); 
            }
        } else {
            System.out.println("Received null list of words.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textDisplay.requestFocus();
        
        ObservableList<Integer> timerOptions = FXCollections.observableArrayList(15, 30, 45, 60);
        timerChoiceBox.setItems(timerOptions);
        timerChoiceBox.setValue(60);

        List<String> wordList = loadWordList("wordsList.txt");
        ObservableList<Integer> wordOptions = FXCollections.observableArrayList(10, 25, 50, 100, wordList.size());
        wordChoiceBox.setItems(wordOptions);

        wordChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Update the displayed words when the choice changes
                loadTest();
            }
        });

        wordChoiceBox.setValue(wordList.size());

        //stopwatchButton.setOnAction(event -> toggleStopwatch());
        updateElapsedTimeLabel();

        loadTest();

        //the timeline is set up but not started until explicitly triggered.
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> change()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        // Set up the resultButton click event handler
    }

    private List<String> loadWordList(String fileName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return words;
    }

    
    void updateElapsedTime() {
        elapsedSecs++;
        if (elapsedSecs == 60) {
            elapsedMins++;
            elapsedSecs = 0;
        }
        updateElapsedTimeLabel();
        // Calculate words per minute and write to file
        int totalWords = totalChar / 5;
        int wpm = (int) Math.round((double) totalWords / (double) ((elapsedMins * 60) + elapsedSecs / 60.0));
        double acc = (double) (totalChar - errorCount) / totalChar * 100;
        writeAccToFile(acc);
        writeWPMToFile(wpm);
        // Print statement to check elapsed time changes
        System.out.println("Elapsed Time: " + elapsedMins + ":" + elapsedSecs);
    }

    private void writeAccToFile(double acc){
        try {
            // Write the words per minute to the file
            accuracyWriter.write(Integer.toString((int)acc));
            accuracyWriter.newLine();
            accuracyWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeWPMToFile(int wpm) {
        try {
            // Write the words per minute to the file
            wpmWriter.write(Integer.toString(wpm));
            wpmWriter.newLine();
            wpmWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startTimer() {
        // Use the selected timer duration from the ChoiceBox
        secs = timerChoiceBox.getValue();
        mins = secs / 60; // Convert seconds to minutes
        secs = secs % 60; // Get the remaining seconds
    
        updateTimeLabel();
    
        // Create a KeyFrame that updates the timer every second
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            //secs--;
    
            // Update your UI with the current time
            updateTimeLabel();
    
            // Check if the timer has reached 0
            if (secs <= 0 && mins <= 0) {
                stopTimer(); // Stop the timer
                switchSceneToResult(); // Switch to results scene
                secondsRemaining = 0; // Ensure secondsRemaining is set to 0
            }
        });
    
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    

    void stopTimer() {
        //timeline.pause();
        timeline.pause();
        try {
            // Close the FileWriter for words per minute
            wpmWriter.close();
            accuracyWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateTimeLabel() {
        timeLabel.setText(String.format("%02d:%02d", mins, secs));
    }

    void updateElapsedTimeLabel() {
        elapsedTimeLabel.setText(String.format("%02d:%02d", elapsedMins, elapsedSecs));
    }
    
    public void quit(ActionEvent event) {
        stage = (Stage) mainPage.getScene().getWindow();
        stage.close();
    }

    public void refresh(ActionEvent event) {
        quit(event);
        Platform.runLater( () -> {
            try {
                new App().start( new Stage() );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } );
    }

    @FXML
    void switchSceneToResult() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Results.fxml"));
            Parent root = loader.load();
    
            // Since we need to pass values to the resultscontroller we need to do this first
            ResultController controller = loader.getController();
            controller.initializeMyData(secondsRemaining, errorCount, totalChar, elapsedMins, elapsedSecs);
            controller.setCurrentWords(enteredWords);
    
            Stage currentStage = (Stage) textDisplay.getScene().getWindow(); // Assuming textDisplay is part of the current scene
            Scene scene = new Scene(root);
    
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void switchToLogin(ActionEvent event) throws IOException{
        App a = new App();
        a.changeScene("loginPage.fxml");
    }

    public void switchToLeaderboard(ActionEvent event) throws IOException {
        App a = new App();
        a.changeScene("leaderboard.fxml");
    }

    public void displayCurrentUser(String username) {
        displayCurrentUser.setText(username); 
    }
}
