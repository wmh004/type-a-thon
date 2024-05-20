package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MixFiles2 {

    public MixFiles2() {
        mixFiles("src\\main\\java\\com\\example\\wordsList.txt", "src\\main\\java\\com\\example\\NumbersList.txt", "mixedText2.txt");
    }
    public static void mixFiles(String wordsFile, String NumbersFile, String outputFile) {
        List<String> words = readFile(wordsFile);
        List<String> numbers = readFile(NumbersFile);

        List<String> mixedText = mixWordsAndNumbers(words, numbers);

        writeToFile(outputFile, mixedText);
    }

    public static List<String> readFile(String fileName) {
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

    public static void writeToFile(String fileName, List<String> lines) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String line : lines) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> mixWordsAndNumbers(List<String> words, List<String> numbers) {
        List<String> mixedText = new ArrayList<>();
        List<String> combinedList = new ArrayList<>();

        combinedList.addAll(words);
        combinedList.addAll(numbers);

        // Shuffle the combined list
        Collections.shuffle(combinedList);

        mixedText.addAll(combinedList);

        return mixedText;
    }
}

