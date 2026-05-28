package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;
import ru.yandex.practicum.exceptions.GameAlreadyFinishedException;
import ru.yandex.practicum.exceptions.NoAvailableWordsException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionaryException;

import java.util.List;

public class WordleGameTest {

    @Test
    void testMakeGuessReturnsAllPlusesWhenWordIsCorrect() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);
        String expected = "+++++";

        String actual = game.makeGuess("кошка");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testMakeGuessNormalizesInput() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);
        String expected = "+++++";

        String actual = game.makeGuess(" КОШКА ");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testMakeGuessThrowsExceptionWhenGuessIsNull() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        try {
            game.makeGuess(null);
            Assertions.fail("makeGuess() должен выбрасывать исключение если guess == null");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("guess не может быть null", e.getMessage());
        }
    }

    @Test
    void testMakeGuessThrowsExceptionWhenGuessIsEmpty() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        try {
            game.makeGuess("  ");
            Assertions.fail("makeGuess должен выбрасывать исключение если guess пустое слово");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Слово не может быть пустым", e.getMessage());
        }
    }

    @Test
    void testMakeGuessThrowsExceptionWhenGuessHasDifferentLength() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        try {
            game.makeGuess("дом");
            Assertions.fail("makeGuess должен выбросить исключение если длина слов разная");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Длина слова не совпадает с длиной ответа", e.getMessage());
        }
    }

    @Test
    void testMakeGuessThrowsExceptionWhenWordNotFoundInDictionary() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        try {
            game.makeGuess("маска");
            Assertions.fail("makeGuess должен выбросить исключение если слова нет в словаре");
        } catch (WordNotFoundInDictionaryException e) {
            Assertions.assertEquals("Слово отсутствует в словаре!", e.getMessage());
        }
    }

    @Test
    void testMakeGuessThrowsExceptionWhenGameAlreadyFinished() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        String firstResult = game.makeGuess("кошка");
        Assertions.assertEquals("+++++", firstResult);

        try {
            game.makeGuess("кошка");
            Assertions.fail("makeGuess должен выбросить исключение если игра окончена");
        } catch (GameAlreadyFinishedException e) {
            Assertions.assertEquals("Игра закончена!", e.getMessage());
        }
    }

    @Test
    void testNewGameStartsWithZeroStepsAndNotFinished() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        Assertions.assertEquals(0, game.getSteps());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void testMakeGuessIncrementsStepsAfterValidGuess() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        game.makeGuess("кошка");

        Assertions.assertEquals(1, game.getSteps());
    }

    @Test
    void testMakeGuessSetsFinishedWhenWordIsCorrect() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        Assertions.assertFalse(game.isFinished());

        game.makeGuess("кошка");

        Assertions.assertTrue(game.isFinished());
    }

    @Test
    void testMakeGuessDoesNotIncrementStepsAfterInvalidGuess() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        try {
            game.makeGuess("  ");
            Assertions.fail("makeGuess должен выбрасывать исключение если guess пустое слово");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Слово не может быть пустым", e.getMessage());
        }
        Assertions.assertEquals(0, game.getSteps());
    }

    @Test
    void testMakeGuessReturnsHintWhenGuessIsValidButWrong() {
        String answer = "кошка";
        String guess = "маска";
        List<String> words = List.of(answer, guess);
        WordleDictionary dictionary = new WordleDictionary(words);
        WordleGame game = new WordleGame(dictionary, answer);
        String expected = "---++";

        String actual = game.makeGuess(guess);

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(1, game.getSteps());
        Assertions.assertFalse(game.isFinished());
    }

    @Test
    void testConstructorNormalizesAnswer() {
        String guess = "кошка";
        String answer = " КОШКА ";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary, answer);
        String expected = "+++++";

        String actual = game.makeGuess(guess);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testConstructorThrowsExceptionWhenAnswerIsNull() {
        String answer = null;
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));

        try {
            new WordleGame(dictionary, answer);
            Assertions.fail("Конструктор должен выбрасывать исключение если answer == null");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("answer не может быть null", e.getMessage());
        }
    }

    @Test
    void testConstructorThrowsExceptionWhenAnswerIsEmpty() {
        String answer = "  ";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));

        try {
            new WordleGame(dictionary, answer);
            Assertions.fail("Конструктор должен выбрасывать исключение если слово пустое");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Слово не может быть пустым", e.getMessage());
        }
    }

    @Test
    void testConstructorThrowsExceptionWhenAnswerNotFoundInDictionary() {
        String answer = "маска";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));

        try {
            new WordleGame(dictionary, answer);
            Assertions.fail("Конструктор должен выбрасывать исключение если answer отсутствует в словаре");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("answer отсутствует в словаре!", e.getMessage());
        }
    }

    @Test
    void testConstructorThrowsExceptionWhenDictionaryIsNull() {
        String answer = "кошка";
        WordleDictionary dictionary = null;

        try {
            new WordleGame(dictionary, answer);
            Assertions.fail("Должно выбрасываться исключение если словарь == null");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Словарь не должен быть null", e.getMessage());
        }
    }

    @Test
    void testConstructorThrowsExceptionWhenDictionaryIsEmpty() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of());

        try {
            new WordleGame(dictionary, answer);
            Assertions.fail("Должно выбрасываться исключение если словарь пуст");
        } catch (EmptyDictionaryException e) {
            Assertions.assertEquals("Словарь пуст!", e.getMessage());
        }
    }

    @Test
    void testMakeGuessSavesAttemptAndHint() {
        String answer = "кошка";
        String guess = "маска";
        List<String> words = List.of(answer, guess);
        WordleDictionary dictionary = new WordleDictionary(words);
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess(guess);

        Assertions.assertEquals(List.of("маска"), game.getAttempts());
        Assertions.assertEquals(List.of("---++"), game.getHints());
    }

    @Test
    void testMakeGuessSavesNormalizedAttempt() {
        String answer = "кошка";
        String guess = " МАСКА ";
        List<String> words = List.of(answer, guess);
        WordleDictionary dictionary = new WordleDictionary(words);
        WordleGame game = new WordleGame(dictionary, answer);

        String actual = game.makeGuess(guess);

        Assertions.assertEquals("---++", actual);
        Assertions.assertEquals(List.of("маска"), game.getAttempts());
        Assertions.assertEquals(List.of("---++"), game.getHints());
    }

    @Test
    void testGetAttemptsReturnsUnmodifiableList() {
        String answer = "кошка";
        String guess = " МАСКА ";
        List<String> words = List.of(answer, guess);
        WordleDictionary dictionary = new WordleDictionary(words);
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess(guess);

        List<String> attempts = game.getAttempts();

        try {
            attempts.add("собака");
            Assertions.fail("Список attempts должен быть неизменяемым");
        } catch (UnsupportedOperationException e) {
            // ожидаемое поведение
        }

        Assertions.assertEquals(List.of("маска"), game.getAttempts());
    }

    @Test
    void testGetHintsReturnsUnmodifiableList() {
        String answer = "кошка";
        String guess = "маска";
        List<String> words = List.of(answer, guess);
        WordleDictionary dictionary = new WordleDictionary(words);
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess(guess);

        List<String> hints = game.getHints();

        try {
            hints.add("+++++");
            Assertions.fail("Список hints должен быть неизменяемым");
        } catch (UnsupportedOperationException e) {
            // ожидаемое поведение
        }

        Assertions.assertEquals(List.of("---++"), game.getHints());
    }

    @Test
    void testSuggestWordReturnsFirstWordWhenNoAttempts() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка", "маска"));
        WordleGame game = new WordleGame(dictionary, answer);

        String actual = game.suggestWord();

        Assertions.assertEquals("кошка", actual);
    }

    @Test
    void testSuggestWordSkipsAlreadyAttemptedWords() {
        String answer = "маска";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка", "маска"));
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess("кошка");

        String actual = game.suggestWord();

        Assertions.assertEquals("маска", actual);
    }

    @Test
    void testSuggestWordSkipsWordsThatDoNotMatchHistory() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of("маска", "лапка", "кошка"));
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess("маска");

        String actual = game.suggestWord();

        Assertions.assertEquals("кошка", actual);
    }

    @Test
    void testSuggestWordDoesNotReturnSameSuggestionTwice() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка", "маска"));
        WordleGame game = new WordleGame(dictionary, answer);

        String firstSuggestion = game.suggestWord();
        String secondSuggestion = game.suggestWord();

        Assertions.assertEquals("кошка", firstSuggestion);
        Assertions.assertEquals("маска", secondSuggestion);
    }

    @Test
    void testSuggestWordThrowsExceptionWhenAllSuggestionsWereAlreadyUsed() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary, answer);

        String firstSuggestion = game.suggestWord();

        Assertions.assertEquals("кошка", firstSuggestion);

        try {
            game.suggestWord();
            Assertions.fail("Должно выбрасываться исключение если все подсказки использованы");
        } catch (NoAvailableWordsException e) {
            Assertions.assertEquals("Нет доступных слов для подсказки!", e.getMessage());
        }
    }

    @Test
    void testSuggestWordThrowsExceptionWhenGameAlreadyFinished() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess("кошка");

        try {
            game.suggestWord();
            Assertions.fail("suggestWord должен выбрасывать исключение если игра окончена");
        } catch (GameAlreadyFinishedException e) {
            Assertions.assertEquals("Игра закончена!", e.getMessage());
        }
    }

    @Test
    void testNewGameStartsWithSixRemainingSteps() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        Assertions.assertEquals(6, game.getRemainingSteps());
    }

    @Test
    void testMakeGuessDecreasesRemainingStepsAfterValidGuess() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        game.makeGuess("кошка");

        Assertions.assertEquals(5, game.getRemainingSteps());
    }

    @Test
    void testMakeGuessDoesNotDecreaseRemainingStepsAfterInvalidGuess() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        try {
            game.makeGuess("      ");
            Assertions.fail("makeGuess должен выбрасывать исключение если guess пустое слово");
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Слово не может быть пустым", e.getMessage());
        }

        Assertions.assertEquals(6, game.getRemainingSteps());
    }

    @Test
    void testGameEndsAfter6IncorrectValidAttempts() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of(
                "кошка", "маска", "лапка", "банка", "рамка", "шапка", "парта"
        ));
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess("маска");
        game.makeGuess("лапка");
        game.makeGuess("банка");
        game.makeGuess("рамка");
        game.makeGuess("шапка");
        game.makeGuess("парта");

        Assertions.assertTrue(game.isFinished());
        Assertions.assertEquals(6, game.getSteps());
        Assertions.assertEquals(0, game.getRemainingSteps());
    }

    @Test
    void testNewGameStartsNotWon() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        Assertions.assertFalse(game.isWon());
    }

    @Test
    void testMakeGuessSetsWonWhenWordIsCorrect() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary);

        game.makeGuess("кошка");

        Assertions.assertTrue(game.isWon());
        Assertions.assertTrue(game.isFinished());
    }

    @Test
    void testGameIsNotWonAfter6IncorrectValidAttempts() {
        String answer = "кошка";
        WordleDictionary dictionary = new WordleDictionary(List.of(
                "кошка", "маска", "лапка", "банка", "рамка", "шапка", "парта"
        ));
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess("маска");
        game.makeGuess("лапка");
        game.makeGuess("банка");
        game.makeGuess("рамка");
        game.makeGuess("шапка");
        game.makeGuess("парта");

        Assertions.assertTrue(game.isFinished());
        Assertions.assertFalse(game.isWon());
        Assertions.assertEquals(0, game.getRemainingSteps());
    }

    @Test
    void testGetAnswerReturnsNormalizedAnswer() {
        WordleDictionary dictionary = new WordleDictionary(List.of("кошка"));
        WordleGame game = new WordleGame(dictionary, " КОШКА ");

        Assertions.assertEquals("кошка", game.getAnswer());
    }

    @Test
    void testGetStateReturnsAttemptsHintsAndSteps() {
        String answer = "кошка";
        String guess = "маска";
        WordleDictionary dictionary = new WordleDictionary(List.of(answer, guess));
        WordleGame game = new WordleGame(dictionary, answer);

        game.makeGuess(guess);

        String state = game.getState();

        Assertions.assertTrue(state.contains("История ходов:"));
        Assertions.assertTrue(state.contains("маска"));
        Assertions.assertTrue(state.contains("---++"));
        Assertions.assertTrue(state.contains("Ход: 1"));
        Assertions.assertTrue(state.contains("Осталось попыток: 5"));
    }
}
