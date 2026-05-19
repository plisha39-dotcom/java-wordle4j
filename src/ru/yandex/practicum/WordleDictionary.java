package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        List<String> newWords = new ArrayList<>();
        for (String word : words) {
            String newWord = normalizeWord(word);
            if (!newWord.isEmpty()) {
                newWords.add(newWord);
            }
        }
        this.words = newWords;
    }

    public String normalizeWord(String word) {
        return word.trim().toLowerCase();
    }

    public boolean contains(String word) {
        String normalizedWord = normalizeWord(word);
        return words.contains(normalizedWord);
    }

    public List<String> getWords() {
        return List.copyOf(words);
    }

    public int size() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public String getRandomWord() {
        if (isEmpty()) {
            throw new EmptyDictionaryException("Словарь пуст!");
        }
        Random random = new Random();
        int randomIndex = random.nextInt(words.size());
        return words.get(randomIndex);
    }
}

