package com.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class mainpageMode2controller implements Initializable {

    @FXML
    private Label timeLabel;

    @FXML
    private Button resultButton;

    @FXML
    private TextArea textDisplay;

    @FXML 
    private Button punctuationButton;

    @FXML
    private Button numbersButton;

    @FXML
    private ChoiceBox<Integer> timerChoiceBox;

    @FXML
    private ChoiceBox<Integer> wordChoiceBox;

    @FXML
    private Button stopwatchButton;

    @FXML
    private Label elapsedTimeLabel;

    @FXML
    private Button quotesButton;

    @FXML
    private Button SDbutton;


    public int errorCount;

    public int totalChar;

    public int secondsRemaining;

    private char expectedKey;

    private char typedKey;

    private boolean stopwatchRunning = false;

    private List<String> enteredWords;

    private List<String> currentWords;

    List<String> quotesListFiles = Arrays.asList("src\\main\\java\\com\\example\\quotesList.txt", "src\\main\\java\\com\\example\\quotes2List.txt", "src\\main\\java\\com\\example\\quotes3List.txt");
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

    @FXML
    void loadTest() {
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
                    }
                }
            }
        });

        textDisplay.setEditable(false);
        enteredWords = List.of(textDisplay.getText().split("\\s+"));
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

    private String getRandomQuote(List<String> quotesList) {
        // Choose a random quote from the list
        if (quotesList.isEmpty()) {
            throw new IllegalArgumentException("quotesList is empty");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(quotesList.size());
        return quotesList.get(randomIndex);
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


    void handleBackspace() {
        if (indexOfLine > 0) {
            indexOfLine--;
            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
            textDisplay.selectRange(indexOfLine, indexOfLine + 1);
        }
    }

    //This part isnt functioning yet
    /*void validateWord() {
        // Extract the entire word from the textDisplay
        String word = textDisplay.getText().substring(0, indexOfLine).trim();
    
        // Split arr to get an array of words
        String[] words = arr.split("\\s+");
    
        if (indexOfLine < words.length) {
            // Check if the entire word is correct
            if (word.equals(words[indexOfLine])) {
                // Turn the entire word green
                textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
            } else {
                // Turn the entire word red
                textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
            }
    
            indexOfLine++;
            textDisplay.selectRange(indexOfLine, indexOfLine + 1);
        } else {
            // Allow typing beyond the last index
            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
            indexOfLine++;
            textDisplay.selectRange(indexOfLine, indexOfLine + 1);
        }
    }*/
    

    void change() {
        // Countdown logic
        if (timerStarted) {
            if (secs == 0) {
                if (mins == 0) {
                    stopTimer(); // Stop the timer when the countdown is complete
                    textDisplay.setEditable(false);
                    textDisplay.setFocusTraversable(false);
                    textDisplay.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                    // Additional logic if needed after countdown completion
                } else {
                    mins--;
                    secs = 59;
                }
            } else {
                //secs--;
    
                // Update secondsRemaining
                secondsRemaining = mins * 60 + secs;
    
                updateTimeLabel();
    
                // Update elapsed time
                updateElapsedTime();
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
                    @Override
                    public void handle(KeyEvent event) {
                        if (!timerStarted) {
                            startTimer();
                            timerStarted = true;
                        }

                        if (event.getCharacter().equals(" ")) {
                            // Handle space character
                            //validateWord();
                            textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: #2196f3;");
                            indexOfLine++;
                            textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                        } else if (event.getCharacter().equals("\b")) {
                            handleBackspace();
                        } else {
                            expectedKey = wordsText.charAt(indexOfLine);
                            typedKey = event.getCharacter().charAt(0);

                            if (indexOfLine < wordsText.length()) {
                                if (typedKey != expectedKey) {
                                    errorCount++;
                                    indexOfLine++;
                                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: red;");
                                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                                } else {
                                    indexOfLine++;
                                    textDisplay.setStyle("-fx-highlight-fill: #bbdefb; -fx-highlight-text-fill: green;");
                                    textDisplay.selectRange(indexOfLine, indexOfLine + 1);
                                }
                            }
                            if (!event.getCharacter().equals(" ")) {
                                totalChar++;
                            }

                            if (indexOfLine == wordsText.length()) {
                                stopTimer();
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

        stopwatchButton.setOnAction(event -> toggleStopwatch());
        updateElapsedTimeLabel();

        loadTest();

        //the timeline is set up but not started until explicitly triggered.
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> change()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(false);

        // Set up the resultButton click event handler
        /*resultButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switchSceneToResult(event);
            }
        });*/
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

    @FXML
    void toggleStopwatch() {
        if (stopwatchRunning) {
            System.out.println("Stopwatch paused");
            timeline.pause();
            stopwatchRunning = false;
            stopwatchButton.setText("Start");
        } else {
            System.out.println("Stopwatch started");
            // Reset elapsed time when starting the stopwatch
            elapsedMins = 0;
            elapsedSecs = 0;

            // Reset the countdown timer if it's already started
            if (timerStarted) {
                timerStarted = false;
                secs = timerChoiceBox.getValue();
                mins = secs / 60; // Convert seconds to minutes
                secs = secs % 60; // Get the remaining seconds
                updateTimeLabel();
            }

            timeline.play();
            stopwatchRunning = true;
            stopwatchButton.setText("Stop");
        }
    }


    void updateElapsedTime() {
        elapsedSecs++;
        if (elapsedSecs == 60) {
            elapsedMins++;
            elapsedSecs = 0;
        }
        updateElapsedTimeLabel();
    
        // Print statement to check elapsed time changes
        System.out.println("Elapsed Time: " + elapsedMins + ":" + elapsedSecs);
    }

    void startTimer() {
        // Use the selected timer duration from the ChoiceBox
        secs = timerChoiceBox.getValue();
        mins = secs / 60; // Convert seconds to minutes
        secs = secs % 60; // Get the remaining seconds
    
        updateTimeLabel();
    
        // Create a KeyFrame that updates the timer every second
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            secs--;
    
            // Update your UI with the current time
            updateTimeLabel();
    
            // Check if the timer has reached 0
            if (secs <= 0) {
                stopTimer(); // Stop the timer
                switchSceneToResult(); // Switch to results scene
            }
        });
    
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void stopTimer() {
        //timeline.pause();
        timeline.stop();
    }

    void updateTimeLabel() {
        timeLabel.setText(String.format("%02d:%02d", mins, secs));
    }

    void updateElapsedTimeLabel() {
        elapsedTimeLabel.setText(String.format("%02d:%02d", elapsedMins, elapsedSecs));
    }

    public void initializeDataWithoutChangingUI(int secondsRemaining, int errorCount, int totalChar) {
        // Set up the initial data without modifying the UI components
        this.errorCount = errorCount;
        this.totalChar = totalChar;
        this.secondsRemaining = secondsRemaining;

        // Additional initialization logic if needed

        // You can print debug information to verify that the data is correctly initialized
        System.out.println("Initialized data without changing UI: " +
                "secondsRemaining=" + secondsRemaining +
                ", errorCount=" + errorCount +
                ", totalChar=" + totalChar);
    }
    

    void switchSceneToResult() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Results.fxml"));
            Parent root = loader.load();
    
            // Since we need to pass values to the resultscontroller we need to do this first
            ResultController controller = loader.getController();
            controller.initializeMyData(secondsRemaining, errorCount, totalChar);
            controller.setCurrentWords(enteredWords);
    
            Stage currentStage = (Stage) textDisplay.getScene().getWindow(); // Assuming textDisplay is part of the current scene
            Scene scene = new Scene(root);
    
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

