package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.ValidationException;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
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

    public User(int id, String email, String login, String name, LocalDate birthday) {
        validation(login);
        this.id = id;
        this.email = email;
        this.login = login;
        if (name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    private void validation(String login) {
        if (login.contains(" ")) {
            throw new ValidationException("This login contains space");
        }
    }
}
