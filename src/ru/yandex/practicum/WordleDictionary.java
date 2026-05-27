package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryException;

import java.util.*;

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

    public String compare(String secret, String guess) {
        if (secret == null || guess == null) {
            throw new IllegalArgumentException("Secret и guess не должны быть null");
        }
        if (secret.length() != guess.length()) {
            throw new IllegalArgumentException("Слова разной длины");
        }

        char[] result = new char[secret.length()];
        Map<Character, Integer> letter = new HashMap<>();

        for (int i = 0; i < result.length; i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                result[i] = '+';
            } else {
                char c = secret.charAt(i);
                letter.put(c, letter.getOrDefault(c, 0) + 1);
            }
        }
        for (int i = 0; i < result.length; i++) {
            if (result[i] == '+') {
                continue;
            }
            char current = guess.charAt(i);
            if (letter.containsKey(current) && letter.get(current) > 0) {
                result[i] = '^';
                letter.put(current, letter.get(current) - 1);
            } else {
                result[i] = '-';
            }
        }
        return new String(result);
    }
}

