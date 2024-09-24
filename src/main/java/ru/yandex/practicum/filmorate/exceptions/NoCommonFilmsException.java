package ru.yandex.practicum.filmorate.exceptions;

public class NoCommonFilmsException extends RuntimeException {
    public NoCommonFilmsException(String message) {
        super(message);
    }
}
