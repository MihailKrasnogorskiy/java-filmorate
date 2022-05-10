package ru.yandex.practicum.filmorate.exceptions;

//класс исключения при валидации
public class ValidationException extends RuntimeException {

    public ValidationException(String s) {
        super(s);
    }
}
