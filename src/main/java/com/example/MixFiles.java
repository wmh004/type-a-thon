package com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MixFiles {

    public MixFiles() {
        mixFiles("src\\main\\java\\com\\example\\wordsList.txt", "src\\main\\java\\com\\example\\PunctuationList.txt", "mixedText.txt");
    }
    public static void mixFiles(String wordsFile, String punctuationFile, String outputFile) {
        List<String> words = readFile(wordsFile);
        List<String> punctuation = readFile(punctuationFile);

        List<String> mixedText = mixWordsAndPunctuation(words, punctuation);

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

    public static List<String> mixWordsAndPunctuation(List<String> words, List<String> punctuation) {
        List<String> mixedText = new ArrayList<>();
        List<String> combinedList = new ArrayList<>();

        combinedList.addAll(words);
        combinedList.addAll(punctuation);

        // Shuffle the combined list
        Collections.shuffle(combinedList);

        mixedText.addAll(combinedList);

        return mixedText;
    }
}

