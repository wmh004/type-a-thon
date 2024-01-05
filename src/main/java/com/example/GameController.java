package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController implements Initializable {


    private int wordCounter = 0;
    private int first = 1;

    private File saveData;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    @FXML
    public Text seconds;
    @FXML
    private Text wordsPerMin;
    @FXML
    private Text accuracy;
    @FXML
    private Text programWord;
    @FXML
    private Text secondProgramWord;

    @FXML
    private TextField userWord;

    @FXML
    private Button playAgain;

    ArrayList<String> words = new ArrayList<>();

    // add words to array list
    public void addToList() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new
                    FileReader("wordsList"));
            String line = reader.readLine();
            while (line != null) {
                words.add(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toMainMenu(ActionEvent ae) throws IOException {
        App m = new App();
        m.changeScene("mainpage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playAgain.setVisible(false);
        playAgain.setDisable(true);
        seconds.setText("60");
        addToList();
        Collections.shuffle(words);
        programWord.setText(words.get(wordCounter));
        secondProgramWord.setText(words.get(wordCounter+1));
        wordCounter++;

        //userWord.getScene().getStylesheets().add(getClass().getResource("gameDisplay.css").toExternalForm());

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        saveData = new File("src/data/"+formatter.format(date).strip()+".txt");

        try {
            if (saveData.createNewFile()) {
                System.out.println("File created: " + saveData.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int timer = 60;  //game will last 60 seconds

    Runnable r = new Runnable() {
        @Override
        public void run() {  //all of this happens within the 1 second 
            if (timer > -1) {
                seconds.setText(String.valueOf(timer));  //this is just a timer
                timer -= 1;
            }

            else {  //this is what happens after the 60 seconds (THIS PART NEEDS SOME CHANGING)
                if (timer == -1) {
                    userWord.setDisable(true);
                    userWord.setText("Game over");

                    try { //this whole block is to write the data into the saveData file
                        FileWriter myWriter = new FileWriter(saveData);
                        myWriter.write(countAll +";");
                        myWriter.write(counter +";");
                        myWriter.write(String.valueOf(countAll-counter));
                        myWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (timer == -4) {
                    playAgain.setVisible(true);
                    playAgain.setDisable(false);
                    executor.shutdown();
                }

                timer -= 1;
            }
        }
    };

    private int countAll = 0;
    private int counter = 0;

    public void startGame(KeyEvent ke) {

        // only gets called once
        if (first == 1) {  //if its one, then the user has already started the game
            first = 0; //if its zero, then the user is already playing
            executor.scheduleAtFixedRate(r, 0, 1, TimeUnit.SECONDS); //does the task each second
        }

        if (ke.getCode().equals(KeyCode.ENTER)) {

            String s = userWord.getText();
            String real = programWord.getText();
            countAll++;

            // if correct
            if (s.equals(real)) {
                counter++;
                wordsPerMin.setText(String.valueOf(counter));

                

            }
            userWord.setText("");
            accuracy.setText(String.valueOf(Math.round((counter*1.0/countAll)*100)));
            programWord.setText(words.get(wordCounter));
            secondProgramWord.setText(words.get(wordCounter+1));
            wordCounter++;
        }

    }
}


