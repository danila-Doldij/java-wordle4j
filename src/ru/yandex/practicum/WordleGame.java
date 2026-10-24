package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WordleGame {
    private String answer;
    private int attemptsLeft;
    private final WordleDictionary dictionary;
    private final List<String> history;
    private final PrintWriter logger;

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        this.dictionary = dictionary;
        this.logger = logger; // Сохраняем переданный логгер
        this.history = new ArrayList<>();
        this.attemptsLeft = 6;
        this.answer = dictionary.getRandomWord();
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public boolean isGameOver() {
        return attemptsLeft <= 0 || history.contains(answer);
    }

    public boolean hasWon() {
        return history.contains(answer);
    }

    public String checkGuess(String guess) throws WordNotFoundInDictionaryException {
        String normalizedGuess = guess.trim().toLowerCase().replace('ё', 'е');

        if (!dictionary.contains(normalizedGuess)) {
            throw new WordNotFoundInDictionaryException("Такого слова нет в словаре!");
        }

        if (history.contains(normalizedGuess)) {
            throw new WordNotFoundInDictionaryException("Вы уже пробовали это слово!");
        }

        history.add(normalizedGuess);
        attemptsLeft--;

        if (normalizedGuess.equals(answer)) {
            return "+++++";
        }

        char[] result = new char[5];
        boolean[] usedInAnswer = new boolean[5];
        boolean[] usedInGuess = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (normalizedGuess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                usedInAnswer[i] = true;
                usedInGuess[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (usedInGuess[i]) continue;

            for (int j = 0; j < 5; j++) {
                if (!usedInAnswer[j] && normalizedGuess.charAt(i) == answer.charAt(j)) {
                    result[i] = '^';
                    usedInAnswer[j] = true;
                    usedInGuess[i] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result[i] != '+' && result[i] != '^') {
                result[i] = '-';
            }
        }

            if (this.logger != null) {
                this.logger.println("Ход игрока: " + normalizedGuess + " -> Результат: " + new String(result));
                this.logger.flush();
            }

        return new String(result);
    }

    public String getHint() {
        List<String> allWords = dictionary.getAllWords();
        if (allWords.isEmpty()) {
            return null;
        }
        int randomIndex = new java.util.Random().nextInt(allWords.size());
        return allWords.get(randomIndex);
    }

    // Метод только для тестов.
    void setAnswerForTest(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}