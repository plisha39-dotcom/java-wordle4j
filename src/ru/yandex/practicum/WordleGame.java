package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.EmptyDictionaryException;
import ru.yandex.practicum.exceptions.GameAlreadyFinishedException;
import ru.yandex.practicum.exceptions.NoAvailableWordsException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionaryException;

import java.util.ArrayList;
import java.util.List;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

public class WordleGame {
    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private boolean finished;
    private final List<String> attempts;
    private final List<String> hints;

    public WordleGame(WordleDictionary dictionary) {
        validateDictionary(dictionary);
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 0;
        this.finished = false;
        this.attempts = new ArrayList<>();
        this.hints = new ArrayList<>();
    }

    public WordleGame(WordleDictionary dictionary, String answer) {
        validateDictionary(dictionary);
        if (answer == null) {
            throw new IllegalArgumentException("answer не может быть null");
        }
        String normalizedAnswer = dictionary.normalizeWord(answer);
        if (normalizedAnswer.isEmpty()) {
            throw new IllegalArgumentException("Слово не может быть пустым");
        }
        if (!dictionary.contains(normalizedAnswer)) {
            throw new IllegalArgumentException("answer отсутствует в словаре!");
        }
        this.dictionary = dictionary;
        this.answer = normalizedAnswer;
        this.steps = 0;
        this.finished = false;
        this.attempts = new ArrayList<>();
        this.hints = new ArrayList<>();
    }

    public String makeGuess(String guess) {
        if (finished) {
            throw new GameAlreadyFinishedException("Игра закончена!");
        }
        if (guess == null) {
            throw new IllegalArgumentException("guess не может быть null");
        }
        String normalizedGuess = dictionary.normalizeWord(guess);
        if (normalizedGuess.isEmpty()) {
            throw new IllegalArgumentException("Слово не может быть пустым");
        }
        if (normalizedGuess.length() != answer.length()) {
            throw new IllegalArgumentException("Длина слова не совпадает с длиной ответа");
        }
        if (!dictionary.contains(normalizedGuess)) {
            throw new WordNotFoundInDictionaryException("Слово отсутствует в словаре!");
        }
        String hint = dictionary.compare(answer, normalizedGuess);
        attempts.add(normalizedGuess);
        hints.add(hint);
        steps++;
        if (normalizedGuess.equals(answer)) {
            finished = true;
        }
        return hint;
    }

    private void validateDictionary(WordleDictionary dictionary) {
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не должен быть null");
        }
        if (dictionary.isEmpty()) {
            throw new EmptyDictionaryException("Словарь пуст!");
        }
    }

    public int getSteps() {
        return steps;
    }

    public boolean isFinished() {
        return finished;
    }

    public List<String> getAttempts() {
        return List.copyOf(attempts);
    }

    public List<String> getHints() {
        return List.copyOf(hints);
    }

    public String suggestWord() {
        for (String word : dictionary.getWords()) {
            if (attempts.contains(word)) {
                continue;
            }
            if (word.length() != answer.length()) {
                continue;
            }
            if (!matchesHistory(word)) {
                continue;
            }
            return word;
        }
        throw new NoAvailableWordsException("Нет доступных слов для подсказки!");
    }

    private boolean matchesHistory(String candidate) {
        for (int i = 0; i < attempts.size(); i++) {
            String attempt = attempts.get(i);
            String hint = hints.get(i);
            String generatedHint = dictionary.compare(candidate, attempt);
            if (!generatedHint.equals(hint)) {
                return false;
            }
        }
        return true;
    }
}
