package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        List<String> newWords = new ArrayList<>();
        for (String word : words) {
        String newWord = normalizeWord(word);
        newWords.add(newWord);
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
        return words;
    }

    public int size() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }
}
