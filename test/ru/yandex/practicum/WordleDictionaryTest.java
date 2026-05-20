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

        Assertions.assertEquals(List.of("кошка", "дом", "собака"), actualWords);
    }

    @Test
    void testConstructorRemovesEmptyLines() {
        List<String> words = List.of("кошка", "  ", "", "дом");

        WordleDictionary dictionary = new WordleDictionary(words);

        Assertions.assertEquals(2, dictionary.size());
        Assertions.assertTrue(dictionary.contains("кошка"));
        Assertions.assertTrue(dictionary.contains("дом"));
    }

    @Test
    void testContainsWorksWithNormalization() {
        List<String> words = List.of("кошка", "дом");

        WordleDictionary dictionary = new WordleDictionary(words);

        Assertions.assertTrue(dictionary.contains(" КОШКА "));
        Assertions.assertTrue(dictionary.contains("Дом"));
        Assertions.assertFalse(dictionary.contains("собака"));
    }

    @Test
    void testGetWordsReturnsACopy() {
        List<String> words = List.of("кошка", "дом");

        WordleDictionary dictionary = new WordleDictionary(words);

        List<String> newList = dictionary.getWords();

        try {
            newList.add("собака");
            Assertions.fail("Список из getWords() должен быть неизменяемым.");
        } catch (UnsupportedOperationException e) {
            // ожидаемое поведение
        }

        Assertions.assertEquals(2, dictionary.size());
        Assertions.assertFalse(dictionary.contains("собака"));
    }

    @Test
    void testGetRandomWordThrowsAnExceptionOnAnEmptyDictionary() {
        WordleDictionary dictionary = new WordleDictionary(List.of());
        try {
            dictionary.getRandomWord();
            Assertions.fail("getRandomWord() должен выбрасывать исключение для пустого словаря");
        } catch (EmptyDictionaryException e) {
            Assertions.assertEquals("Словарь пуст!", e.getMessage());
        }
    }
}
