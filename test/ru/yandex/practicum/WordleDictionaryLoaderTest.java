package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordleDictionaryLoaderTest {
    @TempDir
    Path tempDir;

    @Test
    void testSuccessfullyLoadingDictionaryFromFile() throws IOException {
        Path testFile = tempDir.resolve("words.txt");
        try (Writer fileWriter = new FileWriter(testFile.toString(), StandardCharsets.UTF_8)) {
            fileWriter.write("кошка");
            fileWriter.write("\n");
            fileWriter.write("дом");
            fileWriter.write("\n");
            fileWriter.write("собака");
        }
        PrintWriter log = new PrintWriter(System.out);

        WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader(testFile.toString(), log);
        WordleDictionary dictionary = dictionaryLoader.load();

        Assertions.assertEquals(3, dictionary.size());
        Assertions.assertTrue(dictionary.contains("кошка"));
        Assertions.assertTrue(dictionary.contains("дом"));
        Assertions.assertTrue(dictionary.contains("собака"));
    }

    @Test
    void testLoadThrowsExceptionWhenFileDoesNotExist() {
        Path missingFile = tempDir.resolve("missing_words.txt");
        PrintWriter log = new PrintWriter(System.out);
        WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader(missingFile.toString(), log);

        try {
            dictionaryLoader.load();
            Assertions.fail("load() должен выбрасывать DictionaryLoadException, если файл не найден");
        } catch (DictionaryLoadException e) {
            Assertions.assertEquals("Не удалось загрузить словарь!", e.getMessage());
            Assertions.assertNotNull(e.getCause());
        }
    }

    @Test
    void testLoadThrowsExceptionWhenDictionaryFileIsEmpty() throws IOException {
        Path testFile = tempDir.resolve("words.txt");
        PrintWriter log = new PrintWriter(System.out);
        WordleDictionaryLoader loader = new WordleDictionaryLoader(testFile.toString(), log);
        Files.createFile(testFile);
        try {
            loader.load();
            Assertions.fail("load() должен выбрасывать EmptyDictionaryException для пустого файла");
        } catch (EmptyDictionaryException e) {
            Assertions.assertEquals("Словарь пуст!", e.getMessage());
        }
    }
}
