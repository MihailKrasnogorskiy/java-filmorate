package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @Pattern(regexp = "\\S*$")
    private String login;
    @NotNull
    private String name;
    @Past
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    private Set<Long> likedFilms = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if (name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    public Set<Long> getFriends() {
        return friends;
    }

    public Set<Long> getLikedFilms() {
        return likedFilms;
    }
}
