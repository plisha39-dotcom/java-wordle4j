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

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
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
                    System.out.println("Выберите действие:");
                    System.out.println("1 - Ввести слово");
                    System.out.println("2 - Подсказка");
                    System.out.println("0 - Выход");

                    String command = scanner.nextLine().trim();

                    switch (command) {
                        case "1": {
                            System.out.println("Введите слово: ");
                            String input = scanner.nextLine();
                            try {
                                String mask = game.makeGuess(input);

                                System.out.println("Результат: " + mask);
                                System.out.println("Ход: " + game.getSteps());

                                if (game.isFinished()) {
                                    System.out.println("Поздравляем! Вы угадали слово!");
                                    log.println("Игра завершена, общее количество ходов: " + game.getSteps());
                                }
                            } catch (WordNotFoundInDictionaryException | IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                                log.println("Ошибка ввода слова: " + e.getMessage());
                            } catch (GameAlreadyFinishedException e) {
                                System.out.println(e.getMessage());
                                log.println("Попытка хода после завершения игры: " + e.getMessage());
                            }
                            break;
                        }
                        case "2": {
                            try {
                                String suggested = game.suggestWord();
                                System.out.println("Возможный вариант: " + suggested);
                            } catch (NoAvailableWordsException | GameAlreadyFinishedException e) {
                                System.out.println(e.getMessage());
                                log.println("Ошибка получения подсказки: " + e.getMessage());
                            }
                            break;
                        }
                        case "0":
                            System.out.println("Выход из игры");
                            log.println("Пользователь завершил игру.");
                            return;
                        default:
                            System.out.println("Неизвестная команда");
                            break;
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
}
