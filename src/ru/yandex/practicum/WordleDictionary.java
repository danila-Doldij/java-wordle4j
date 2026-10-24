package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {
    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public int size() {
        return words.size();
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст!");
        }
        return words.get(new Random().nextInt(words.size()));
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }
        String normalized = word.trim().toLowerCase().replace('ё', 'е');
        return words.contains(normalized);
    }

    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }
}