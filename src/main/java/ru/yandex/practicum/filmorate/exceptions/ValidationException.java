package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//класс исключения при валидации
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BusinessException {

    public ValidationException(String s) {
        super(s);
    }
}
