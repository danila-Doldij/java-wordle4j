package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    public WordleDictionary load(String fileName) {
        List<String> validWords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String cleanWord = line.trim().toLowerCase().replace('ё', 'е');

                if (cleanWord.length() == 5) {
                    validWords.add(cleanWord);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла словаря: " + e.getMessage(), e);
        }

        return new WordleDictionary(validWords);
    }
}