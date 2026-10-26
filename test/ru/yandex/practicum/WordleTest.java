package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    private WordleGame game;
    private List<String> testDictionaryList;

    @BeforeEach
    void setUp() {
        testDictionaryList = new ArrayList<>();
        testDictionaryList.add("герой");
        testDictionaryList.add("гонец");
        testDictionaryList.add("банан");

        WordleDictionary fakeDictionary = new WordleDictionary(testDictionaryList);
        game = new WordleGame(fakeDictionary, null);
        game.setAnswerForTest("герой");
    }

    @Test
    void testWin_WhenCorrectWordIsGuessed() throws Exception {
        String result = game.checkGuess("герой");
        assertEquals("+++++", result);
        assertTrue(game.hasWon());
    }

    @Test
    void testLogic_ExampleFromTask() throws Exception {
        String result = game.checkGuess("гонец");
        assertEquals("+^-^-", result);
        assertFalse(game.hasWon());
    }

    @Test
    void testException_WhenWordNotInDictionary() {
        List<String> tinyDict = new ArrayList<>();
        tinyDict.add("герой");
        WordleDictionary isolatedDict = new WordleDictionary(tinyDict);
        WordleGame isolatedGame = new WordleGame(isolatedDict, null);
        isolatedGame.setAnswerForTest("герой");

        Exception exception = assertThrows(WordNotFoundInDictionaryException.class, () -> {
            isolatedGame.checkGuess("абвгд");
        });

        assertTrue(exception.getMessage().contains("нет в словаре"));
    }
}