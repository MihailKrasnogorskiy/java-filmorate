package ru.yandex.practicum.filmorate.model.films.genre;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
// класс жанра
@Getter
public class Genre {
    private final int id;
    private Genres name;

    @JsonCreator
    public Genre(int id) {
        this.id = id;
    }

    public void setName(Genres name) {
        this.name = name;
    }
}
