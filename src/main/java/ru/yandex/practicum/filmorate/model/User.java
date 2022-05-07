package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
//класс пользователя
public class User {
    private long id;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @NotNull
    private String login;
    @NotNull
    private String name;
    @Past
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        validation(login);
        this.email = email;
        this.login = login;
        if (name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    //валидация логина
    private void validation(String login) {
        if (login.contains(" ")) {
            throw new ValidationException("This login contains space");
        }
    }
}
