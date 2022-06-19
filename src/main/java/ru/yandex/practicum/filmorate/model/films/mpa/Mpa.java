package ru.yandex.practicum.filmorate.model.films.mpa;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class Mpa {
    private final int id;
    private String name;

    @JsonCreator
    public Mpa(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
