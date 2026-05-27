package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;
import ru.yandex.practicum.exceptions.GameAlreadyFinishedException;
import ru.yandex.practicum.exceptions.NoAvailableWordsException;
import ru.yandex.practicum.exceptions.WordNotFoundInDictionaryException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter("wordle.log", StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in)) {
            try {
                WordleDictionaryLoader loader = new WordleDictionaryLoader("words_ru.txt", log);
                WordleDictionary dictionary = loader.load();
                WordleGame game = new WordleGame(dictionary);
                log.println("Игра успешно создана. Размер словаря: " + dictionary.size());
                while (!game.isFinished()) {
                    printPrompt();
                    String input = scanner.nextLine();
                    String command = input.trim();
                    if (command.equals("0")) {
                        System.out.println("Выход из игры");
                        System.out.println("Загаданное слово: " + game.getAnswer());
                        return;
                    }
                    if (command.isEmpty()) {
                        try {
                            String suggested = game.suggestWord();
                            System.out.println("Возможный вариант: " + suggested);
                        } catch (NoAvailableWordsException | GameAlreadyFinishedException e) {
                            System.out.println(e.getMessage());
                            log.println("Ошибка получения возможного варианта: " + e.getMessage());
                        }
                        continue;
                    }
                    try {
                        game.makeGuess(input);
                        System.out.println(game.getState());
                        if (game.isFinished()) {
                            printFinalResult(game);
                        }
                    } catch (WordNotFoundInDictionaryException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                        log.println("Ошибка ввода слова: " + e.getMessage());
                    } catch (GameAlreadyFinishedException e) {
                        System.out.println(e.getMessage());
                        log.println("Попытка хода после завершения игры: " + e.getMessage());
                    }
                }
            } catch (DictionaryLoadException e) {
                System.out.println("Не удалось загрузить словарь: " + e.getMessage());
                log.println("Ошибка загрузки словаря: " + e.getMessage());
                e.printStackTrace(log);
            } catch (EmptyDictionaryException e) {
                System.out.println("Словарь пуст: " + e.getMessage());
                log.println("Словарь пуст: " + e.getMessage());
                e.printStackTrace(log);
            } catch (RuntimeException e) {
                System.out.println("Произошла непредвиденная ошибка программы: " + e.getMessage());
                log.println("Непредвиденная ошибка: " + e.getMessage());
                e.printStackTrace(log);
            }
        } catch (IOException e) {
            System.out.println("Не удалось создать лог-файл: " + e.getMessage());
        }
    }

    private static void printPrompt() {
        System.out.println();
        System.out.println("Введите слово из 5 букв.");
        System.out.println("Нажмите Enter без ввода, чтобы получить возможный вариант.");
        System.out.println("Введите 0 для выхода.");
        System.out.print("> ");
    }

    private static void printFinalResult(WordleGame game) {
        if (game.isWon()) {
            System.out.println("Поздравляем! Вы угадали слово!");
        } else {
            System.out.println("Попытки закончились. Вы проиграли.");
        }
        System.out.println("Загаданное слово: " + game.getAnswer());
    }
}

