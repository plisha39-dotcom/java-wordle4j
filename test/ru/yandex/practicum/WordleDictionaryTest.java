package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.util.ArrayList;
import java.util.List;

class WordleDictionaryTest {

    @Test
    void testConstructorNormalizesWords() {
        List<String> words = new ArrayList<>();
        words.add(" КОШКА ");
        words.add("дом");
        words.add("СОБАКА");

        WordleDictionary dictionary = new WordleDictionary(words);
        List<String> actualWords = dictionary.getWords();

        Assertions.assertEquals(List.of("кошка"), actualWords);
    }

    @Test
    void testConstructorRemovesEmptyLines() {
        List<String> words = List.of("кошка", "  ", "", "дом");

        WordleDictionary dictionary = new WordleDictionary(words);

        Assertions.assertEquals(1, dictionary.size());
        Assertions.assertTrue(dictionary.contains("кошка"));
        Assertions.assertFalse(dictionary.contains("дом"));
    }

    @Test
    void testContainsWorksWithNormalization() {
        List<String> words = List.of("кошка", "дом");

        WordleDictionary dictionary = new WordleDictionary(words);

        Assertions.assertTrue(dictionary.contains(" КОШКА "));
        Assertions.assertFalse(dictionary.contains("Дом"));
        Assertions.assertFalse(dictionary.contains("собака"));
    }

    @Test
    void testGetWordsReturnsUnmodifiableList() {
        List<String> words = List.of("кошка", "дом");

        WordleDictionary dictionary = new WordleDictionary(words);

        List<String> newList = dictionary.getWords();

        try {
            newList.add("собака");
            Assertions.fail("Список из getWords() должен быть неизменяемым.");
        } catch (UnsupportedOperationException e) {
            // ожидаемое поведение
        }

        Assertions.assertEquals(1, dictionary.size());
        Assertions.assertFalse(dictionary.contains("собака"));
    }

    @Test
    void testGetRandomWordThrowsExceptionWhenDictionaryIsEmpty() {
        WordleDictionary dictionary = new WordleDictionary(List.of());
        try {
            dictionary.getRandomWord();
            Assertions.fail("getRandomWord() должен выбрасывать исключение для пустого словаря");
        } catch (EmptyDictionaryException e) {
            Assertions.assertEquals("Словарь пуст!", e.getMessage());
        }
    }

    @Test
    void testCompareHandlesRepeatedLettersInGuess() {
        String secret = "лапка";
        String guess = "папка";
        List<String> words = List.of(secret, guess);
        WordleDictionary dictionary = new WordleDictionary(words);

        String expected = "-++++";

        String actual = dictionary.compare(secret, guess);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCompareReturnsAllPlusesWhenWordsAreEqual() {
        String secret = "кошка";
        String guess = "кошка";
        List<String> words = List.of(secret, guess);
        WordleDictionary dictionary = new WordleDictionary(words);

        String expected = "+++++";

        String actual = dictionary.compare(secret, guess);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCompareHandlesRepeatedLettersInSecret() {
        String secret = "касса";
        String guess = "скала";
        List<String> words = List.of(secret, guess);
        WordleDictionary dictionary = new WordleDictionary(words);

        String expected = "^^^-+";

        String actual = dictionary.compare(secret, guess);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testCompareThrowsExceptionWhenWordsHaveDifferentLength() {
        String secret = "кошка";
        String guess = "кош";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        try {
            dictionary.compare(secret, guess);
            Assertions.fail("Должно выпасть исключение если длина слов разная");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Слова разной длины", e.getMessage());
        }
    }

    @Test
    void testCompareThrowsExceptionWhenSecretIsNull() {
        String secret = null;
        String guess = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        try {
            dictionary.compare(secret, guess);
            Assertions.fail("Должно выпасть исключение если слово null");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Secret и guess не должны быть null", e.getMessage());
        }
    }

    @Test
    void testCompareThrowsExceptionWhenGuessIsNull() {
        String secret = "кошка";
        String guess = null;
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        try {
            dictionary.compare(secret, guess);
            Assertions.fail("Должно выпасть исключение если слово null");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Secret и guess не должны быть null", e.getMessage());
        }
    }

    @Test
    void testNormalizeWordReplacesYoWithE() {
        WordleDictionary dictionary = new WordleDictionary(List.of("берег"));

        String actual = dictionary.normalizeWord(" БЕРЁГ ");

        Assertions.assertEquals("берег", actual);
    }

    @Test
    void testConstructorReplacesYoWithE() {
        WordleDictionary dictionary = new WordleDictionary(List.of("берёг"));

        Assertions.assertTrue(dictionary.contains("берег"));
        Assertions.assertTrue(dictionary.contains("берёг"));
    }
}
