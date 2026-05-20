package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final String filename;
    private final PrintWriter log;

    public WordleDictionaryLoader(String filename, PrintWriter log) {
        this.filename = filename;
        this.log = log;
    }

    public WordleDictionary load() {
        List<String> words = new ArrayList<>();
        log.println("Начинаем загрузку словаря: " + filename);
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                words.add(line);
            }
        } catch (IOException e) {
            log.println("Ошибка загрузки словаря: " + e.getMessage());
            e.printStackTrace(log);
            throw new DictionaryLoadException("Не удалось загрузить словарь!", e);
        }
        WordleDictionary dictionary = new WordleDictionary(words);
        if (dictionary.isEmpty()) {
            log.println("Словарь пуст!");
            throw new EmptyDictionaryException("Словарь пуст!");
        } else {
            log.println("Словарь успешно загружен. Количество слов: " + dictionary.size());
        }
        return dictionary;
    }
}
