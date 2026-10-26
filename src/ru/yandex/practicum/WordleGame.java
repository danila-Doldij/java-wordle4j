package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleGame {
    private String answer;
    private int attemptsLeft;
    private final WordleDictionary dictionary;
    private final List<String> history;
    private final List<String> resultsHistory;
    private final PrintWriter logger;

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        this.dictionary = dictionary;
        this.logger = logger;
        this.history = new ArrayList<>();
        this.resultsHistory = new ArrayList<>();
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

        String result = calculateResult(normalizedGuess);

        history.add(normalizedGuess);
        resultsHistory.add(result);
        attemptsLeft--;

        if (normalizedGuess.equals(answer)) {
            return "+++++";
        }

        if (this.logger != null) {
            this.logger.println("Ход игрока: " + normalizedGuess + " -> Результат: " + result);
            this.logger.flush();
        }

        return result;
    }

    private String calculateResult(String normalizedGuess) {
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

        return new String(result);
    }

    public String getHint() {
        List<String> allWords = dictionary.getAllWords();
        if (allWords.isEmpty()) {
            return null;
        }

        List<String> candidates = new ArrayList<>();

        for (String word : allWords) {
            boolean isValid = true;

            for (int i = 0; i < history.size(); i++) {
                String guess = history.get(i);
                String result = resultsHistory.get(i);

                if (word.equals(guess)) {
                    isValid = false;
                    break;
                }

                for (int j = 0; j < 5; j++) {
                    char guessChar = guess.charAt(j);
                    char resultChar = result.charAt(j);

                    if (resultChar == '+') {
                        if (word.charAt(j) != guessChar) {
                            isValid = false;
                            break;
                        }
                    } else if (resultChar == '^') {
                        if (word.charAt(j) == guessChar) {
                            isValid = false;
                            break;
                        }

                        boolean hasLetterElsewhere = false;
                        for (int k = 0; k < 5; k++) {
                            if (k != j && word.charAt(k) == guessChar) {
                                hasLetterElsewhere = true;
                                break;
                            }
                        }
                        if (!hasLetterElsewhere) {
                            isValid = false;
                            break;
                        }
                    } else if (resultChar == '-') {
                        if (word.charAt(j) == guessChar) {
                            isValid = false;
                            break;
                        }
                    }
                }
                if (!isValid) break;
            }

            if (isValid) {
                candidates.add(word);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        int randomIndex = new Random().nextInt(candidates.size());
        return candidates.get(randomIndex);
    }

    void setAnswerForTest(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}