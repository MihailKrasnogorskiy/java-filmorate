package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class IllegalHttpMethodException extends BusinessException {

    public IllegalHttpMethodException(String s) {
        super(s);
    }
}
