package ru.yandex.practicum;

import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {
    public static void main(String[] args) {
        try (PrintWriter logger = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream("game.log"), StandardCharsets.UTF_8))) {

            logger.println("Игра запущена.");
            logger.flush();

            WordleDictionaryLoader loader = new WordleDictionaryLoader();

            try {
                WordleDictionary dictionary = loader.load("words_ru.txt");

                if (dictionary.size() == 0) {
                    System.out.println("Ошибка: Словарь пуст!");
                    return;
                }

                WordleGame game = new WordleGame(dictionary, logger);

                if (logger != null) {
                    logger.println("Словарь загружен. Размер: " + dictionary.size());
                    logger.println("Игра началась. Попыток осталось: " + game.getAttemptsLeft());
                    logger.flush();
                }

                System.out.println("Добро пожаловать в Wordle!");
                System.out.println("У вас есть 6 попыток, чтобы угадать слово из 5 букв.");
                System.out.println("Правила:");
                System.out.println("+ — буква есть и стоит на своём месте");
                System.out.println("^ — буква есть, но стоит не там");
                System.out.println("- — такой буквы нет в слове");
                System.out.println("Если хотите подсказку — нажмите Enter.");
                System.out.println("----------------------------------------");

                Scanner scanner = new Scanner(System.in);

                while (!game.isGameOver()) {
                    System.out.print("Ваш ход (осталось попыток: " + game.getAttemptsLeft() + "): ");
                    String input = scanner.nextLine();

                    if (input.trim().isEmpty()) {
                        String hintWord = game.getHint();

                        if (hintWord == null) {
                            System.out.println("К сожалению, словарь пуст, нет подсказок.");
                            continue;
                        }

                        System.out.println("Вы приняли подсказку: '" + hintWord + "'");

                        try {
                            String result = game.checkGuess(hintWord);

                            System.out.println(hintWord);
                            System.out.println(result);

                            if (game.hasWon()) {
                                System.out.println("Поздравляем! Подсказка оказалась верной!");
                            }
                        } catch (WordNotFoundInDictionaryException e) {
                            System.out.println("Ошибка при использовании подсказки: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Произошла ошибка: " + e.getMessage());
                            if (logger != null) {
                                logger.println("Критическая ошибка: " + e.getMessage());
                                logger.flush();
                            }
                        }
                        continue;
                    }

                    try {
                        String result = game.checkGuess(input);
                        System.out.println(input);
                        System.out.println(result);
                    } catch (WordNotFoundInDictionaryException e) {
                        System.out.println("Ошибка: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Произошла ошибка: " + e.getMessage());
                        if (logger != null) {
                            logger.println("Критическая ошибка: " + e.getMessage());
                            logger.flush();
                        }
                    }
                }

                if (game.hasWon()) {
                    System.out.println("Поздравляем! Вы угадали слово!");
                    System.out.println("Загаданное слово было: " + game.getAnswer());

                    if (logger != null) {
                        logger.println("Результат: ПОБЕДА!");
                        logger.flush();
                    }
                } else {
                    System.out.println("К сожалению, попытки закончились.");
                    System.out.println("Загаданное слово было: " + game.getAnswer());

                    if (logger != null) {
                        logger.println("Результат: ПОРАЖЕНИЕ.");
                        logger.flush();
                    }
                }

            } catch (Exception e) {
                System.out.println("Критическая ошибка при запуске: " + e.getMessage());
                if (logger != null) {
                    logger.println("Критическая ошибка при запуске: " + e.getMessage());
                    logger.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при создании лог-файла: " + e.getMessage());
        }
    }
}