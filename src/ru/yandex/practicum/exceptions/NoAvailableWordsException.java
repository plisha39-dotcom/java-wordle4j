package ru.yandex.practicum.exceptions;

public class NoAvailableWordsException extends RuntimeException {
    public NoAvailableWordsException(String message) {
        super(message);
    }
}